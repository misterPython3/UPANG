package com.example;
import java.util.Random;
import java.util.Scanner;
public class App 
{
    public static String getInput(Scanner s,String name,String regex){
        String res;
        do{
            System.out.print(name);
            res = s.next();
        }while(!res.matches(regex));
        return res;
    }
    public static class Pig{
        private Random rand;
        private Random bot_rand; // have separate instance to have its own seed
        private Scanner sc;
        private boolean gameOver;
        private int[] board;
        public Pig(){
            rand = new Random(); 
            bot_rand = new Random(); 
            sc = new Scanner(System.in);
        }
        public int roll(){
            return rand.nextInt(6)+1; // generates random int 1 <= x <= 6
        }
        public int turn(int index,String player_name){
            int sum = 0, rollValue;
            do{
                rollValue = roll();
                System.out.printf("The die rolled a %d\n",rollValue);
                if(rollValue==1){
                    sum = 0;
                    break;
                }
                sum += rollValue;
                if(board[index]+sum >= 100){
                    gameOver = true;
                    break;
                }
            }while(getInput(sc,String.format("[ %s ] Roll Again(Y/N)?",player_name,index+1),"^([Yy]|[Nn])$").equalsIgnoreCase(("y")));
            return sum;
        }
        public boolean botRollYN(){
            return bot_rand.nextInt(128)%2==0;
        }
        public int botTurn(int index){
            int sum = 0, rollValue;
            boolean botAns;
            do{
                rollValue = roll();
                System.out.printf("The die rolled a %d\n",rollValue);
                if(rollValue == 1){
                    sum = 0;
                    break;
                }
                sum+= rollValue;
                if(board[index]+sum>=100){
                    gameOver = true;
                    break;
                }
                System.out.print("[ Computer] Roll Again(Y/N)?");
                botAns = botRollYN();
                System.out.println(botAns?"y":"n");
            }while(botAns);
            return sum;
        }
        public void gameInit(int players){
            board = new int[players];
            gameOver= false;
            int i;
            do{
                for(i=0;i<players&&!gameOver;i++){
                System.out.printf("===== [ Player %d's turn ] =====\n",i+1);
                board[i] += turn(i,String.format("Player %d",i+1));
                System.out.printf("===== [ End Turn of Player %d ] =====\n",i+1);
                System.out.printf("Player %d has %d point[s] on their board.\n",i+1,board[i]);
                System.out.println();
            }
            }while(!gameOver);
            System.out.printf("Player %d Won the Pig Game\n",i);
        }
        public void botGameInit(){
            board = new int[2];
            gameOver = false;
            int sum;
            for(;;){
                System.out.println("---- YOUR TURN ----");
                board[0] += sum=turn(0,"Player");
                System.out.printf("Turn ended. You earned %d points, total points %d.\n\n",sum,board[0]);
                if(gameOver){
                    System.out.println("You Won with "+board[0]+" points");
                    break;
                }
                System.out.println("---- COMPUTER'S TURN ----");
                board[1] += sum=botTurn(1);
                
                System.out.printf("Turn ended. Computer earned %d points, total points %d.\n\n",sum,board[1]);
                if(gameOver){
                    
                    System.out.println("Computer Won"+board[1]+" points");
                    break;
                }
            }
            
        }
    }
    public static void main( String[] args )
    {
        Pig game = new Pig();
        game.botGameInit();
    }
}
