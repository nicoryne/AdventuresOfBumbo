package game.util.handlers;

import services.LoggerHelper;

import java.io.*;
import java.util.Objects;

public abstract class MapHandler {

    public static int[][] loadMap(String mapFileName, int maxCol, int maxRow) {
        int[][] mapTile2DArray = new int[maxCol][maxRow];
        String mapFilePath = "/res/maps/" + mapFileName;

        try {
            BufferedReader br = openMapFile(mapFilePath);
            LoggerHelper.logInfo("[MapHandler] Loaded map file: " + mapFileName);
            int col;
            int row = 0;

            while (row < maxRow) {
                String line = br.readLine();

                if (line == null) {
                    break;
                }

                String[] numbers = line.split(" ");
                for (col = 0; col < maxCol; col++) {
                    if (col < numbers.length) {
                        int num = Integer.parseInt(numbers[col]);
                        mapTile2DArray[col][row] = num;
                    } else {
                        mapTile2DArray[col][row] = 0;
                    }
                }

                row++;
            }

            br.close();
        } catch (IOException e) {
            LoggerHelper.logError("Error reading map from file: " + mapFileName, e);
        }

        return mapTile2DArray;
    }

    private static BufferedReader openMapFile(String mapFilePath) throws IOException {
        InputStream is = MapHandler.class.getResourceAsStream(mapFilePath);
        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is));
        return new BufferedReader(isr);
    }
}
