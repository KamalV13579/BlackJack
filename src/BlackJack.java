import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;


public class BlackJack{
    ArrayList<Card> deck; 
    //dealer cards
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerValue;
    int dealerAce;


    //users cards
    ArrayList<Card> userHand;
    int userValue;
    int userAce;


    //Window Initialization
    int width = 600;
    int height = width;

    int cardWidth = 110; //1.4 ratio
    int cardHeight = 154;

    JFrame frame = new JFrame("BlackJack");
    JPanel gamPanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
    
        try{
            //display hidden card
            Image hiddenCardIMG = new ImageIcon(getClass().getResource("./Assets/BACK.png")).getImage();
            if(!standButton.isEnabled()){
                hiddenCardIMG = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
            }
            g.drawImage(hiddenCardIMG, 20,20, cardWidth, cardHeight, null);

            //draw dealer hand
            for(int i=0;i<dealerHand.size();i++){
                Image cardIMG = new ImageIcon(getClass().getResource(dealerHand.get(i).getImagePath())).getImage();
                g.drawImage(cardIMG, cardWidth+30+(cardWidth+10)*i,20 ,cardWidth, cardHeight, null);
            }

            //draw user hand
            for(int i=0;i<userHand.size();i++){
                Image cardIMG = new ImageIcon(getClass().getResource(userHand.get(i).getImagePath())).getImage();
                g.drawImage(cardIMG, 20+(cardWidth+10)*i,300 ,cardWidth, cardHeight, null);
            }

            if(!standButton.isEnabled()){
                String userScore = String.valueOf(userValue);
                String dealerScore = String.valueOf(dealerValue);
                String message = "";
                if(userValue > 21){
                    message= "You Lose";
                }
                else if (dealerValue > 21) {
                    message = "You win";
                }
                else if(userValue==dealerValue){
                    message = "You Tie";
                }
                else if(userValue>dealerValue){
                    message = "You Win";
                }
                else if(dealerValue>userValue){
                    message = "You Lose";
                }

                g.setFont(new Font("Arial",Font.PLAIN,30));
                g.setColor(Color.white);
                g.drawString(message, 220, 250);
                g.drawString(userScore, 100, 500);
                g.drawString(dealerScore, 100, 210);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    };
};
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton standButton = new JButton("Stand");

    BlackJack(){
        startGame();

        frame.setVisible(true);
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamPanel.setLayout(new BorderLayout());
        gamPanel.setBackground(new Color(3,103,4));
        frame.add(gamPanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        standButton.setFocusable(false);
        buttonPanel.add(standButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //draw a card
                Card card = deck.remove(deck.size()-1);
                userValue += card.getValue();
                if (card.isAce()) {
                userAce += 1;
                }
                userHand.add(card);
                if(reduceUserAce() >= 21){
                    hitButton.setEnabled(false);
                }   
                gamPanel.repaint();
            }
        });

        standButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                hitButton.setEnabled(false);
                standButton.setEnabled(false);
                //flip dealer hidden card
                while(dealerValue<17){
                    Card dealerCard = deck.remove(deck.size()-1);
                    dealerValue += dealerCard.getValue();
                    if (dealerCard.isAce()) {
                        dealerAce += 1;
                    }
                    dealerHand.add(dealerCard);
                    reduceDealerAce();
                }
                gamPanel.repaint();

            }
        });

        gamPanel.repaint();

    }
    
    private void startGame(){
        //create starting deck
        buildDeck();
        shuffleDeck();

        //initialize the dealer hand
        dealerHand = new ArrayList<Card>();
        dealerValue = 0;
        dealerAce = 0;
        hiddenCard = deck.remove(deck.size()-1);
        dealerValue = hiddenCard.getValue();
        if (hiddenCard.isAce()) {
            dealerAce += 1;
        }

        Card dealerCard = deck.remove(deck.size()-1);
        dealerValue += dealerCard.getValue();
        if (dealerCard.isAce()) {
            dealerAce += 1;
        }
        dealerHand.add(dealerCard);


        //initialize user cards
        userHand = new ArrayList<Card>();
        userValue = 0;
        userAce = 0;
        for(int i =0; i<2;i++){
            Card card = deck.remove(deck.size()-1);
            userValue += card.getValue();
            if (card.isAce()) {
                userAce += 1;
            }
            userHand.add(card);
        }
    }


    //builds the deck using an iterated loop
    public void buildDeck(){
        deck = new ArrayList<Card>();
        String[] values = {"A","2","3","4","5","6","7","8","9","J","Q","K"};
        String[] type = {"C", "D", "H", "S"};

        for(int i=0; i<values.length; i++){
            for(int j=0; j<type.length; j++){
                Card card = new Card(values[i],type[j]);
                deck.add(card);
            }

        }
    }


    // shuffles the deck using a premade method in java
    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    public int reduceUserAce(){
        while(userValue>21 && userAce>0){
            userValue-=10;
            userAce-=1;
        }
        return userValue;
    }
    public int reduceDealerAce(){
        while(dealerValue>21 && dealerAce>0){
            dealerValue-=10;
            dealerAce-=1;
        }
        return dealerValue;
    }


    //defines the Card class
    private class Card{
        String value;
        String type;

        Card(String value, String type){
            this.value = value;
            this.type = type;
        }
        public int getValue() {
            // TODO Auto-generated method stub
            if("AJQK".contains(value)){
                if (value.equals("A")) {
                    return 11;
                }
                return 10;
            }
            //return value 2-9
            return Integer.parseInt(value);
        }
        public boolean isAce(){
            return value.equals("A");
        }

        public String getImagePath(){
            return "./Assets/" +toString()+".png";
        }
        public String toString(){
            return value + "-" +type;
        }
    }
}

