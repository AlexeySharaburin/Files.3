package FilesThird;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) throws IOException {

        GameProgress game1 = new GameProgress(5, 4, 1, 20.6);
        GameProgress game2 = new GameProgress(4, 3, 3, 38.9);
        GameProgress game3 = new GameProgress(1, 0, 6, 50.7);

        // /Users/alexey/Desktop/Games/GameRunner/savegames : Путь к папке с бэкапами

        List<String> listStringsNames = new ArrayList<>();

        File dirGameRunner = new File("/Users/alexey/Desktop/Games/GameRunner");
        checkDir(dirGameRunner, "/Users/alexey/Desktop/Games/GameRunner");

        File dirSavegames = new File("/Users/alexey/Desktop/Games/GameRunner/savegames");
        checkDir(dirSavegames, "/Users/alexey/Desktop/Games/GameRunner/savegames");

        File save1 = new File("/Users/alexey/Desktop/Games/GameRunner/savegames", "save1.dat");
        checkFile(save1, "save1.dat");
        saveGame(game1, "/Users/alexey/Desktop/Games/GameRunner/savegames/save1.dat");
        listStringsNames.add("/Users/alexey/Desktop/Games/GameRunner/savegames/save1.dat");

        File save2 = new File("/Users/alexey/Desktop/Games/GameRunner/savegames/", "save2.dat");
        checkFile(save2, "save2.dat");
        saveGame(game2, "/Users/alexey/Desktop/Games/GameRunner/savegames/save2.dat");
        listStringsNames.add("/Users/alexey/Desktop/Games/GameRunner/savegames/save2.dat");

        File save3 = new File("/Users/alexey/Desktop/Games/GameRunner/savegames", "save3.dat");
        checkFile(save3, "save3.dat");
        saveGame(game3, "/Users/alexey/Desktop/Games/GameRunner/savegames/save3.dat");
        listStringsNames.add("/Users/alexey/Desktop/Games/GameRunner/savegames/save3.dat");


        zipFiles(listStringsNames, "/Users/alexey/Desktop/Games/GameRunner/savegames/zip.zip");

        //        zipFiles1(dirSavegames, "/Users/alexey/Desktop/Games/GameRunner/savegames/zip.zip");

        cleanDir(dirSavegames);




        String pathZip = "/Users/alexey/Desktop/Games/GameRunner/savegames/zip.zip";
        String path = "/Users/alexey/Desktop/Games/GameRunner/savegames";

        String pathFile1 = "/Users/alexey/Desktop/Games/GameRunner/savegames/package_save_1.dat";
        String pathFile2 = "/Users/alexey/Desktop/Games/GameRunner/savegames/package_save_2.dat";
        String pathFile3 = "/Users/alexey/Desktop/Games/GameRunner/savegames/package_save_1.dat";

        openZip(pathZip, path);


        System.out.println("Результаты сохранённых игр:");
        openProgress(pathFile1);
        openProgress(pathFile2);
        openProgress(pathFile3);

    }

    public static void openZip(String pathZip, String path) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(pathZip))) {
            ZipEntry entry;
            String name;

            while((entry = zis.getNextEntry()) != null) {
                name = entry.getName();

                FileOutputStream fos = new FileOutputStream(name);

//                for (int c = zis.read(); c != -1; c = zis.read()) {
//                    fos.write(c);
//                }

                while(zis.available() > 0) {
                    int c = zis.read();
                    fos.write(c);
                }

                fos.flush();
                zis.closeEntry();
                fos.close();

            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openProgress(String pathFile) {
        GameProgress game = null;
        try (FileInputStream fis = new FileInputStream(pathFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            game = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(game.toString());
    }

    public static void zipFiles(List<String> listStrings, String pathZip) throws FileNotFoundException {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathZip))) {
            int i = 1;
            for (String string : listStrings) {
                try (FileInputStream fis = new FileInputStream(string)) {
                    ZipEntry entry = new ZipEntry("/Users/alexey/Desktop/Games/GameRunner/savegames/package_save_" + i + ".dat");
                    zout.putNextEntry(entry);
                    byte[] info = new byte[fis.available()];
                    fis.read(info);
                    zout.write(info);
                    zout.closeEntry();
                    i++;
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Файлы успешно заархивированы.");
    }

    public static void zipFiles1(File dir, String pathZip) throws FileNotFoundException {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathZip))) {
            int i = 1;
            for (File file : dir.listFiles()) {
                try (FileInputStream fis = new FileInputStream(file.getName())) {
                    ZipEntry entry = new ZipEntry("/Users/alexey/Desktop/Games/GameRunner/savegames/package_save_" + i + ".dat");
                    System.out.println(file.getName());
                    zout.putNextEntry(entry);
                    byte[] info = new byte[fis.available()];
                    fis.read(info);
                    zout.write(info);
                    zout.closeEntry();
                    i++;
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Файлы успешно заархивированы.");
    }

    public static void checkDir(File dir, String dirName) {
        if (dir.mkdir()) {
            System.out.println("Директория '" + dirName + "'успешно создана.\n");
        }
    }

    public static void cleanDir(File dir) {
        for (File file : dir.listFiles()) {
            if (!("zip.zip".equals(file.getName()))) {
                file.delete();
            }
        }
        System.out.println("Файлы, не лежашие в архиве, успешно удалены.");
    }

    public static void checkFile(File file, String fileName) {
        try {
            if (file.createNewFile()) {
                System.out.println("Файл '" + fileName + "' успешно создан.\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void saveGame(GameProgress gameProgress, String path) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
