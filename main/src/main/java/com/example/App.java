package com.example;
import java.util.Random;
import java.util.Scanner;


public class App  {

    // MARK - This would be always be here.
    public static void main(String[] args) {
        Pig game = new Pig();
        game.botGameInit();
    }

    public static String getInput(
        Scanner scanner, 
        String name, 
        String pattern
    ){
        String result;

        do {
            System.out.print(name);
            result = scanner.nextLine();
        } while(!result.matches(pattern));

        return result;
    }

    public static class Pig {
        private static final String
        WELCOME_TEXT = "Hello! Welcome to the Pig Game!",
        GOODBYE_TEXT = "Goodbye.\n";
        
        // MARK - `botRandom` have separate instance to have its own seed
        private Random random, botRandom;
        private Scanner scanner;
        private boolean isOver;
        private int[] board;
        public Pig() {
            random = new Random(); 
            botRandom = new Random(); 
            scanner = new Scanner(System.in);
        }

        public int roll() {
            // MARK - generates random int 1 <= x <= 6
            return random.nextInt(6) + 1; 
        }

        public int turn(
            int index, 
            String playerName
        ) {
            int sum = 0, rollValue;
            
            do {
                rollValue = roll();
                System.out.printf("The die rolled a %d\n", rollValue);
                if(rollValue==1){
                    sum = 0;
                    break;
                }

                sum += rollValue;

                if(board[index] + sum >= 100){
                    isOver = true;
                    break;
                }
            } while(
                getInput(
                    scanner,
                    String.format("[ %s ] Keep Going? (Y/N):",
                    playerName,
                    index + 1
                ),"Y|N").equals("Y"))
            ;

            return sum;
        }

        public boolean isRollYN() {
            return botRandom.nextInt(128) % 2==0;
        }

        private int botTurn(int index) {
            int sum = 0, rollValue;
            boolean isBotAnswer;

            do{
                rollValue = roll();

                System.out.printf("The die rolled a %d\n", rollValue);
                if(rollValue == 1) {
                    sum = 0;
                    break;
                }

                sum+= rollValue;
                if( board[index] + sum >= 100 ){
                    isOver = true;
                    break;
                }

                System.out.print("[ Computer ] Keep Going? (Y/N):");
                isBotAnswer = isRollYN();

                System.out.println(isBotAnswer ? "Y":"N");

            } while(isBotAnswer);

            return sum;
        }
        public void gameInit(int players){
            if(players < 2)return;
            int index;
            board = new int[players];
            System.out.println(WELCOME_TEXT);
            while(
                getInput(
                    scanner, 
                    "Do you want to start a new game? (Y/N):",
                     "(Y|N)")
                        .equals("Y")
                     ){
                System.out.println();
                isOver = false;
                do{
                    for(index=0; index < players && !isOver; index++){
                        System.out.printf("===== [ Player %d's turn ] =====\n", index + 1);
                        board[index] += turn(
                            index, 
                            String.format("Player %d", 
                            index + 1)
                        );

                        System.out.printf("===== [ End Turn of Player %d ] =====\n", index + 1);
                        System.out.printf(
                            "Player %d has %d point[s] on their board.\n", 
                            index + 1, 
                            board[index]
                        );
                        System.out.println();
                    }
                } while(!isOver);

                System.out.printf("Player %d Won the Pig Game\n\n", index);
                for(index=0; index < players; index++) board[index] = 0;
            }
            System.out.println();
            System.out.println(GOODBYE_TEXT);
        }

        public void botGameInit(){
            int sum;
            board = new int[2];
            System.out.println(WELCOME_TEXT);
            while(
                getInput(
                    scanner, 
                    "Do you want to start a new game? (Y/N):",
                     "(Y|N)")
                        .equals("Y")
                    ){
                System.out.println();
                isOver = false;
                for(;;){
                    System.out.println("---- YOUR TURN ----");
                    board[0] += sum = turn(0,"Player");

                    System.out.printf(
                        "Turn ended. You earned %d points\n\n",
                        sum
                    );

                    if(isOver){
                        System.out.println("You Won with "+board[0]+" points");
                        break;
                    }

                    System.out.println("---- COMPUTER'S TURN ----");
                    board[1] += sum = botTurn(1);
                    
                    System.out.printf(
                        "Turn ended. Computer earned %d points\n\n",
                        sum
                    );

                    if(isOver){
                        System.out.println("Computer Won"+board[1]+" points");
                        break;
                    }
                }
                System.out.println();
                board[0] = board[1] = 0;
            }
            System.out.println();
            System.out.println(GOODBYE_TEXT);
        }

    }

}
