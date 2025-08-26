package ru.zolo;

import java.io.*;
import java.nio.file.*;

public class SekiroSavePatcher {

    // Сигнатура вокруг нужного места (10 нулей, 0x14 00, 4 байта SP, 8 нулей, 5D 00 00 00, дальше опыт).
    // Мы будем искать этот якорь и брать SP по смещению +12 от его начала, XP по +0x24 (для проверки диапазона).
    private static final byte[] PREFIX = new byte[] {
            0,0,0,0,0,0,0,0,0,0,  // 10 нулей
            0x14,0x00             // 0x14 00
    };
    private static final byte[] MIDDLE_ZEROS = new byte[] {
            0,0,0,0,0,0,0,0       // 8 нулей
    };
    private static final byte[] AFTER = new byte[] {
            0x5D,0x00,0x00,0x00   // 5D 00 00 00
    };
    // Относительные смещения от начала сигнатуры
    private static final int OFFSET_SP = 12;     // int32 LE очки навыков
    private static final int OFFSET_AFTER = 28;  // позиция начала AFTER (5D 00 00 00)
    private static final int OFFSET_XP = 0x24;   // int32 LE опытом (для sanity-check)

    public static void main(String[] args) throws Exception {
        if (args.length < 1 || args.length > 2) {
            System.out.println("Использование: java SekiroSavePatcher <path_to_S0000.sl2> [newSkillPoints]");
            return;
        }
        Path in = Paths.get(args[0]);
        if (!Files.exists(in)) {
            System.err.println("Файл не найден: " + in);
            return;
        }
        int newSP = (args.length >= 2) ? Integer.parseInt(args[1]) : 100;

        byte[] data = Files.readAllBytes(in);

        int anchor = findAnchor(data);
        if (anchor < 0) {
            System.err.println("Не удалось найти сигнатуру блока персонажа. Файл не пропатчен.");
            return;
        }

        // читаем текущие SP и XP
        int spPos = anchor + OFFSET_SP;
        int xpPos = anchor + OFFSET_XP;
        int oldSP = readIntLE(data, spPos);
        int xp    = readIntLE(data, xpPos);

        // простая проверка адекватности: SP в [0..200], XP в [0..50_000_000]
        if (xp < 0 || xp > 50_000_000) {
            System.err.printf("12-Подозрительное значение XP (%d) по смещению 0x%X. Отмена.%n", xp, xpPos);
            return;
        }

        System.out.printf("13-Найден блок: anchor=0x%X, SP=%d @0x%X, XP=%d @0x%X%n",
                anchor, oldSP, spPos, xp, xpPos);

        // создаём копию и записываем новые SP
        Path out = outPath(in);
        Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);

        try (RandomAccessFile raf = new RandomAccessFile(out.toFile(), "rw")) {
            raf.seek(spPos);
            writeIntLE(raf, newSP);
        }

        System.out.printf("14-Готово! Установлено %d очков навыков. Файл: %s%n", newSP, out.toAbsolutePath());
        System.out.println("15-Если игра не примет сейв — напиши, добавим пересчёт CRC слота.");
    }

    private static Path outPath(Path in) {
        String name = in.getFileName().toString();
        int dot = name.lastIndexOf('.');
        String base = (dot > 0) ? name.substring(0, dot) : name;
        String ext  = (dot > 0) ? name.substring(dot) : "";
        return in.resolveSibling(base + "_mod" + ext);
    }

    /** Поиск сигнатуры: [10x00][14 00][SP 4b][8x00][5D 00 00 00] */
    private static int findAnchor(byte[] a) {
        for (int i = 0; i + 64 < a.length; i++) {
            if (!match(a, i, PREFIX)) continue;
            // после PREFIX должны быть 4 байта (SP), потом 8 нулей, потом AFTER
            int posSP = i + PREFIX.length;
            int posZeros = posSP + 4;
            if (!allZeros(a, posZeros, MIDDLE_ZEROS.length)) continue;
            int posAfter = posZeros + MIDDLE_ZEROS.length;
            if (!match(a, posAfter, AFTER)) continue;
            // Доп. проверка: дальше по +0x24 лежит разумный XP
            int xp = readIntLE(a, i + OFFSET_XP);
            if (xp >= 0 && xp <= 50_000_000) {
                return i; // нашли якорь
            }
        }
        return -1;
    }

    private static boolean allZeros(byte[] a, int off, int len) {
        if (off < 0 || off + len > a.length) return false;
        for (int j = 0; j < len; j++) if (a[off + j] != 0) return false;
        return true;
    }

    private static boolean match(byte[] a, int off, byte[] pat) {
        if (off < 0 || off + pat.length > a.length) return false;
        for (int j = 0; j < pat.length; j++) if (a[off + j] != pat[j]) return false;
        return true;
    }

    private static int readIntLE(byte[] a, int off) {
        return (a[off] & 0xFF)
                | ((a[off+1] & 0xFF) << 8)
                | ((a[off+2] & 0xFF) << 16)
                | ((a[off+3] & 0xFF) << 24);
    }

    private static void writeIntLE(RandomAccessFile raf, int v) throws IOException {
        raf.write(v & 0xFF);
        raf.write((v >>> 8) & 0xFF);
        raf.write((v >>> 16) & 0xFF);
        raf.write((v >>> 24) & 0xFF);
    }
}
