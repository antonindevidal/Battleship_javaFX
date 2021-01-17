package view;

import game.Manager.Manager;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
///
/// the aim of this class is to see where the player place his boats
public class Previsualisation implements Serializable {

    private ArrayList<ImageView> previsualisation = new ArrayList<>();
    private GridPane gp;
    private Manager game;
    private static final int NBCASES = 5;

    private boolean destroyed = false;

    public ArrayList<ImageView> getPrevisualisation() { return previsualisation;}

    public boolean isDestroyed() { return destroyed; }

    public Previsualisation(GridPane gp, Manager game) {
        this.gp = gp;
        this.game = game;
    }
    public void createPrevisualisation()
    {
        if(destroyed)
            return;
        for(int i = 0; i<NBCASES; i++) // Create all images view
        {
            ImageView imageView = new ImageView("/images/boatChestWhite.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            previsualisation.add( imageView);

        }
    }


    public void destroyPrevisualisation() // Destroy images view
    {
        for(int i=0; i<NBCASES;i++)
        {
            StackPane n =(StackPane)previsualisation.get(i).getParent();
            if (n!=null)
            {
                n.getChildren().remove(previsualisation.get(i));
            }
        }
        destroyed = true;
    }

    public void refreshPrevisualisation(int x, int y )
    {
        if (game.getPartOfGame() != Manager.jeu.place || destroyed)// avoid errors
            return;
        ObservableList<Node> children = gp.getChildren();
        List<Integer> btp = game.getBoatToPlace();

        boolean isOk = game.getMyBoard().canPlaceShip(x,y,btp.get(0),game.isHorizontal());

        for(int i=0; i<5;i++)
        {
            StackPane n =(StackPane)previsualisation.get(i).getParent();
            if (n!=null)
            {
                n.getChildren().remove(previsualisation.get(i));
            }
        }


        for(int i=0; i<btp.get(0);i++)
        {
            StackPane stackPane = null;
            if(x+i<=9 && game.isHorizontal() || y+i<=9 && !game.isHorizontal())
            {

                if(game.isHorizontal())
                {
                    stackPane = (StackPane) children.get(y*10+x+1+i);
                }else
                {
                    stackPane = (StackPane) children.get((y+i)*10+x+1);
                }

                ColorAdjust ca = new ColorAdjust();
                if (!isOk) // If the boat cant be placed, boat is red
                {

                    ca.setHue(0); // red color
                    ca.setSaturation(10);
                    ca.setContrast(10);

                }
                else // If can place boat, boat is green
                {
                    ca.setHue(0.66);/// green color
                    ca.setSaturation(10);

                }

                int rotate = 0;
                if(game.isHorizontal())
                    rotate = 90;
                previsualisation.get(i).setEffect(ca);
                if (i==0)
                {

                    if(game.isHorizontal())
                        rotate = rotate + 180;
                    previsualisation.get(i).setImage(new Image("/images/frontBoatWhite.png"));// One side of the boat
                }
                else if(i == btp.get(0)-1)
                {
                    if(!game.isHorizontal())
                        rotate = rotate + 180;
                    previsualisation.get(i).setImage(new Image("/images/frontBoatWhite.png")); // Other side of the boat
                }
                else
                {
                    previsualisation.get(i).setImage(new Image("/images/boatChestWhite.png"));// Boat chest
                }
                previsualisation.get(i).setRotate(rotate); // Set rotation of the boat

                stackPane.getChildren().add(previsualisation.get(i));
            }
        }
    }
}
