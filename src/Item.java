package src;

public abstract class Item{
    private int atk;
    private String name;
    private int rarity;
    private String type;
    
    public int getAtk() { return atk; }
    public void setAtk(int atk) { this.atk = atk; }
    public String getName() { return name; }
    public void setName(String name) {this.name = name; }
    public int getRarity() { return rarity; }
    public void setRarity(int rarity) {this.rarity = rarity; }
    public String getType() { return type; }
    public void setType(String name) {this.type = type; }
}