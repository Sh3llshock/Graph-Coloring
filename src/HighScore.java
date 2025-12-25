import java.io.*;
import java.util.*;


// I have named so many things "highscore" I think I'm lost concerning what is what at this point
public class HighScore {

    private final String FILE_NAME; // file where highscores will be stored
    private static final int maxHighscore = 3; // top 3
    private final List<ScoreEntry> highscores;


    public HighScore(String gameMode) {

        this.highscores = new ArrayList<>();

        this.FILE_NAME = gameMode.toLowerCase() + "highscore.txt";
        loadHighscores();
    }

    public List<ScoreEntry> getHighScores() {
        return new ArrayList<>(highscores);
    }
    public static class ScoreEntry {
        private final int score;
        private final String name;
        public ScoreEntry(String name, int score) {

            this.score = score;
            this.name = name;
        }
        public int getScore() {
            return score;
        }
        public String getName() {return "placeholder";}
    }

    // read the file and parse in the highscores
    private void loadHighscores() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null){
                String[] info = line.split(":");
                if (info.length == 2) {
                    int score = Integer.parseInt(info[1].trim());
                    String name = info[0].trim();
                    highscores.add(new ScoreEntry(name, score));
                }

            }
            sortHighScores();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addHighScore(String name, int score) {

        highscores.add(new ScoreEntry(name, score));

        sortHighScores();
        if (highscores.size() > maxHighscore) {

            highscores.remove(highscores.size() - 1);
        }
        saveHighscores();
    }

    public void saveHighscores() {

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (ScoreEntry entry : highscores) {
                writer.println(entry);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getName(){
        return "temporary name";
    }

    // makes sure the largest highscore stays on top
    public void sortHighScores() {
        highscores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
    }
}

