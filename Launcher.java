// Grace Schultz
// 48761302
// Lab Fall-7 Fall

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Launcher {
	public static void main(String[] args){ // main method asks how much money to insert
        int numSpins = 0;
        SlotMachine slot = new SlotMachine();
        Scanner input = new Scanner(System.in);
        System.out.print("How much money would you like to insert? ");
        double money = input.nextDouble(); // saves amount of money inserted
        slot.insertMoney(money);
        while(true) {
            System.out.print("Spin/Quit (S/Q) : "); // asks spin or quit
            String s = input.next();
            if(s.equalsIgnoreCase("Q")) { // case ignores
                break;
            }
            else{
                numSpins++; // adds to number of spins for bonus
                if (numSpins % 5 == 0) {
                    slot.setBonus();
                    slot.play();
                    slot.setRegular();
                }
                else {
                    slot.play();
                }
            }      
        }
        slot.quit();
    }
}
class SlotMachine {
    private MoneyHandler theMoney; // private
    PlayingField playingField;

    public SlotMachine() {
        theMoney = new MoneyHandler();
        setRegular();
    }
    public void insertMoney(double money) {
        theMoney.add(money);
    }
    public void play() {
        double winnings; // sets winnings
        theMoney.deduct(0.25);
        System.out.println("Paid 0.25 to spin..."); // prints how much they spent to spin
        winnings = playingField.spin();
        theMoney.add(winnings);
        System.out.println(theMoney);
    }
    public void quit() {
        theMoney.dispense();
        System.exit(0);
    }
    public void setBonus() {
        playingField = new BonusPlayingField();
    }
    public void setRegular() {
        playingField = new PlayingField();
    }

}
class PlayingField {
    private ArrayList<Wheel> wheels; // array of wheel
    public PlayingField() {
        wheels = new ArrayList<Wheel>();
        wheels.add(new Wheel());
        wheels.add(new Wheel());
        wheels.add(new Wheel());
        wheels.add(new Wheel());
    }
    public double spin() { // sets the spin and layout
        String output[] = new String[3];
        System.out.print("Spun ");
        for (int i = 0; i < output.length; i++) {
            output[i] = wheels.get(i).spin();
            if(i < output.length - 1) {
                System.out.print(output[i] + "-");
            }
           else {
               System.out.print(output[i]);
            }
        }
        System.out.println();
        if (output[0].equals(output[1]) && output[1].equals(output[2]) && output[2].equals(output[0])) {
            System.out.println("Won $1"); // how much you win
            return 1.00;
        }
        else if(output[0].equals(output[1]) || output[1].equals(output[2]) || output[0].equals(output[2])) {
            System.out.println("Won 50 cents"); // how much you win
            return 0.50;
        }
        else{
            System.out.println("No prize"); // how much you win
            return 0.00;
        }
    }
}
class MoneyHandler {
    // adds or deducts money based on how much you spend/lose
    private double balance;
    public MoneyHandler() {
        balance = 0;
    }
    public void add(double money) {
        balance += money;
    }
    public void deduct(double money) {
        balance -= money;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double money) {
        balance = money;
    }
    public void dispense() {
        System.out.printf("Dispensed $ %.2f\n", balance);
        try {
            PrintWriter pw = new PrintWriter("voucher.txt");
            pw.printf("Casino Voucher for $%.2f\n", balance);
            pw.close();
            balance = 0;
        }
        catch(FileNotFoundException e) {
            System.out.println(e);
        }
    }
    public String toString() {
        return "You have a $ " + balance + " balance";
    } // states balance
}
class Wheel {
    private Random rand;
    ArrayList<String> values = new ArrayList<String>();
    public Wheel() {
        rand = new Random();
        try {
            File file = new File("wheelvalues.txt"); // pulls info from file
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) { // takes into consideration the line changes in the file
                values.add(scan.nextLine());
            }
            scan.close(); // closes scanner
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }

    }
    public String spin() {
        return values.get(rand.nextInt(values.size()));
    }
}
class BonusPlayingField extends PlayingField {
    public BonusPlayingField() {

        super();
    }
    @Override
    public double spin() {
        double payout = super.spin();
      int[] bonuses = {2, 2, 3, 3, 4, 4, 5, 5}; // bonus options
      Random rand = new Random();
      int index = rand.nextInt(bonuses.length);
      int value = bonuses[index];
      System.out.printf("Bonus %dX...\n", value);
      payout = value * payout;
      System.out.printf("Adjusted payout %.2f\n", payout);
      return payout; // need to still return payout
    }
}
