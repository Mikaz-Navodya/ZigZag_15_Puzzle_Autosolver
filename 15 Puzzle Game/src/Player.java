
public class Player {
        private String name;
        private int timeTaken; // Time taken in milliseconds
        private String timeInString;
        private int AmountOfMoves;

        public Player(String name, int timeTaken,String timeInString,int AmountOfMoves) {
            this.name = name;
            this.timeTaken = timeTaken;
            this.AmountOfMoves = AmountOfMoves;
            this.timeInString = timeInString;
        }

        public String getName() {
            return name;
        }
        public int Score() {
            return timeTaken+AmountOfMoves;
        }

        public int getTimeTaken() {
            return timeTaken;
        }

    public String getTimeInString() {
        return timeInString;
    }

    public int getAmountOfMoves() {
        return AmountOfMoves;
    }
    
    
        
        
}
