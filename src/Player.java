import java.util.ArrayList;

public class Player {

    private int hp;
    private final int HP_MAX;

    private double baseDamage, baseFireRate, finalDamage, finalFireRate;

    private Active active;
    private Trinket trinket;
    private ArrayList<Passive> passiveList;
    private ArrayList<Collectible> collectList;

    public Player(int hp, double damage, double fireRate) {
        this.hp = this.HP_MAX = hp;
        this.baseDamage = damage;
        this.baseFireRate = fireRate;
        passiveList = new ArrayList<>();
        collectList = new ArrayList<>();
    }

    public void updateStats() {

        finalDamage = baseDamage;
        finalFireRate = baseFireRate;

        for (Passive p : passiveList) {
            finalDamage *= p.getDamageMod();
            finalFireRate *= p.getFireMod();
        }

        if (active != null && active.isLoaded()) {
            finalDamage *= active.getDamageMod();
            finalFireRate *= active.getFireMod();
        }

        if (trinket != null) {
            finalDamage *= trinket.getDamageMod();
            finalFireRate *= trinket.getFireMod();
        }
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    public double getFinalFireRate() {
        return finalFireRate;
    }

    public void viewStats() {
        System.out.printf("HP: %d Danno corrente %.2f2,firerate rate: %.2f \n", hp, finalDamage, finalDamage,
                finalFireRate);
    }

    public void addItem(Item nuovo) {
        if (nuovo instanceof Active) {
            this.active = (Active) nuovo;
        } else if (nuovo instanceof Passive passive) {
            passiveList.add(passive);

        } else {
            this.trinket = (Trinket) nuovo;
        }
    }

    public Trinket dropTrinket() {
        if (this.trinket == null)
            return null;
        Trinket temp = this.trinket;
        this.trinket = null;
        return temp;
    }

    public boolean checkCollectibles() {
        boolean trovati = false;
        for (int i = collectList.size() - 1; i >= 0; i--) {
            Collectible c = collectList.get(i);
            if (c instanceof Trinket) {
                collectList.remove(i);
                trovati = true;
            }

        }
        return trovati;
    }

    public boolean ricarica() {
        boolean trovati = false;
        for (int i = collectList.size() - 1; i >= 0; i--) {
            Collectible c = collectList.get(i);
            if (c instanceof Trinket) {
                collectList.remove(i);
                trovati = true;
            }
        }
        return trovati;
    }

    public boolean cura() {
        if (hp <= 0)
            return false;
        boolean curato = false;
        for (int i = collectList.size() - 1; i >= 0; i--) {
            Collectible c = collectList.get(i);
            if (c instanceof Heart) {
                curato = true;
                if (hp < HP_MAX) {
                    hp += 10;

                }
                if (hp > HP_MAX)
                    hp = HP_MAX;
                collectList.remove(i);
               
            }
            
        }
        return curato;
    }

    public boolean recharge(Battery b) {
        boolean BatteryFound = false;
        for (int i = collectList.size() - 1; i > 0; i++) {
            Collectible c = collectList.get(i);
            if (c instanceof Battery) {
                collectList.remove(c);
                BatteryFound = true;
                this.active.reload();
                return true;
            }
        }
        return BatteryFound;
    }
}
