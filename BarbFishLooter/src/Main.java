import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.GroundItems;
import org.tbot.methods.Time;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.GroundItem;
import org.tbot.wrappers.Tile;


@Manifest(name = "rFish_Looter", authors = "Ryder", description = "Runs to the barbarian village and picks up the fish. Banks them at closest bank. Priority: Salmon>Pike>Trout (Raw)", version = 1.1)
public class Main extends AbstractScript {
    private boolean needToBank(){
        return Inventory.isFull();
    }
    private boolean lootFish(){
        return !Inventory.isFull();
    }
    public boolean onStart(){
        LogHandler.log("-------------------------");
        LogHandler.log("      rFish_Looter");
        LogHandler.log(" Please report any bugs");
        LogHandler.log("-------------------------");
        return true;
    }
    private enum State{
        LOOT,BANK
    }
    private State getState(){
        if(needToBank()){
            return State.BANK;
        }else{
            return State.LOOT;
        }
    }
    @Override
    public int loop(){
        Camera.setPitch(50);
        Camera.setAngle(125);
        switch(getState()){
            case BANK:
                if(needToBank()){
                    Tile bankTile = new Tile(3094, 3491 ,0);
                    Path pathToBankTile = Walking.findPath(bankTile);
                    GameObject bankBooth = GameObjects.getNearest("Bank booth");
                    if(bankBooth != null && bankBooth.isOnScreen()){
                        if(!Bank.isOpen()){
                            bankBooth.interact("Bank");
                            Time.sleep(1250, 2000);
                        }else if(Bank.isOpen()){
                            Bank.depositAll();
                            Time.sleep(350, 1000);
                        }
                    }else{
                        if(pathToBankTile != null){
                            pathToBankTile.traverse();
                            Time.sleep(350, 1000);
                        }
                    }
                }
                break;
            case LOOT:
                GroundItem fish = GroundItems.getNearest("Raw salmon");
                GroundItem fish1 = GroundItems.getNearest("Raw pike");
                GroundItem fish2 = GroundItems.getNearest("Raw trout");
                if(lootFish()){
                    if(fish != null && fish.isOnScreen()){
                        fish.interact("Take");
                        Time.sleep(25, 150);
                    }else if(fish1 != null && fish1.isOnScreen()){
                        fish1.interact("Take");
                        Time.sleep(25, 150);
                    }else if(fish2 != null && fish2.isOnScreen()){
                        fish2.interact("Take");
                        Time.sleep(25, 150);
                    }else{
                        Tile dropSpot = new Tile(3109, 3433, 0);
                        Path pathToFish = Walking.findPath(dropSpot);
                        if(pathToFish != null) {
                            pathToFish.traverse();
                        }
                    }
                }

        }
        return 500;
    }
}
