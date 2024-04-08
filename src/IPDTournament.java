import java.util.*;

public class IPDTournament {
    static final int ROUNDS_PER_GAME = 10;
    static final int NUM_PLAYERS = 30;

    public static void main(String[] args) {
        List<Player> players = initializePlayers();
        Map<Integer, List<Integer>> scoresByStrategy = new HashMap<>();

        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                playGame(players.get(i), players.get(j), scoresByStrategy);
            }
        }

        calculateAndDisplayResults(players, scoresByStrategy);
    }

    static List<Player> initializePlayers() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            players.add(new Player(1));
        }
        for (int i = 0; i < 5; i++) {
            players.add(new Player(2));
        }
        for (int i = 0; i < 19; i++) {
            players.add(new Player(3));
        }
        for (int i = 0; i < 1; i++) {
            players.add(new Player(4));
        }

        return players;
    }

//    static List<Player> initializePlayers() {
//        List<Player> players = new ArrayList<>();
//        for (int i = 0; i < 8; i++) {
//            players.add(new Player(1)); // TYPE 1
//        }
//        for (int i = 0; i < 7; i++) {
//            players.add(new Player(2)); // TYPE 2
//        }
//        for (int i = 0; i < 7; i++) {
//            players.add(new Player(3)); // TYPE 3
//        }
//        for (int i = 0; i < 15; i++) {
//            players.add(new Player(4)); // TYPE 4
//        }




    static void playGame(Player player1, Player player2, Map<Integer, List<Integer>> scoresByStrategy) {
        char lastMoveP1 = 'C', lastMoveP2 = 'C';
        int scoreP1 = 0, scoreP2 = 0;
        for (int round = 0; round < ROUNDS_PER_GAME; round++) {
            char moveP1 = player1.makeMove(lastMoveP2);
            char moveP2 = player2.makeMove(lastMoveP1);

            int roundScoreP1 = player1.calculateScore(moveP1, moveP2);
            int roundScoreP2 = player2.calculateScore(moveP2, moveP1);
            scoreP1 += roundScoreP1;
            scoreP2 += roundScoreP2;

            lastMoveP1 = moveP1;
            lastMoveP2 = moveP2;
        }
        player1.updateTotalScore(scoreP1);
        player2.updateTotalScore(scoreP2);

        scoresByStrategy.computeIfAbsent(player1.strategyType, k -> new ArrayList<>()).add(scoreP1);
        scoresByStrategy.computeIfAbsent(player2.strategyType, k -> new ArrayList<>()).add(scoreP2);
    }

    static void calculateAndDisplayResults(List<Player> players, Map<Integer, List<Integer>> scoresByStrategy) {
        int[] totalScores = new int[5];
        int[] counts = new int[5];

        for (Player player : players) {
            totalScores[player.strategyType] += player.score;
            counts[player.strategyType]++;
        }

        for (int i = 1; i <= 4; i++) {
            System.out.println("Average score for TYPE " + i + ": " +
                    (counts[i] == 0 ? 0 : (double) totalScores[i] / counts[i]));
        }

        for (Map.Entry<Integer, List<Integer>> entry : scoresByStrategy.entrySet()) {
            System.out.println("Scores for TYPE " + entry.getKey() + ": " + entry.getValue());
        }
    }

    static class Player {
        int strategyType;
        int score;
        Random rand = new Random();

        public Player(int strategyType) {
            this.strategyType = strategyType;
            this.score = 0;
        }

        char makeMove(char opponentLastMove) {
            switch (strategyType) {
                case 1: return 'C';
                case 2: return 'D';
                case 3: return rand.nextDouble() < 0.5 ? 'C' : 'D';
                case 4: return opponentLastMove;
                default: return 'C';
            }
        }

        int calculateScore(char myMove, char opponentMove) {
            if (myMove == 'C' && opponentMove == 'C') {
                return 4;
            } else if (myMove == 'C' && opponentMove == 'D') {
                return 0;
            } else if (myMove == 'D' && opponentMove == 'C') {
                return 5;
            } else if (myMove == 'D' && opponentMove == 'D') {
                return 2;
            }
            return 0;
        }

        void updateTotalScore(int score) {
            this.score += score;
        }
    }
}
