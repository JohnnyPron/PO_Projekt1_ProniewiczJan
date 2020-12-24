package agh.cs.project.secondary_objects;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class DNA {
    private int[] genes;

    public int[] getGenes() { return genes; }

    public DNA(){
        this.genes = new int[32];
    }

    public DNA(int[] genes){
        if(genes.length != 32){
            throw new IllegalArgumentException("DNA should be 32 genes long!");
        }
        Arrays.sort(genes);
        this.genes = genes;
    }

    public void setRandomGenes(){
        int[] genes = new int[32];
        for(int i = 0; i < genes.length; i++){
            Random random = new Random();
            genes[i] = random.nextInt(8);
        }
        this.genes = genes;
        mutation();
    }

    public DNA crossingGenes(DNA partnersGenes){
        // należy wylosować dwa miejsca podziału chromosomów (między drugim a przedostatnim genem włącznie)
        int crosspoint1 = new Random().nextInt(30) + 1;
        int crosspoint2 = new Random().nextInt(30) + 1;
        while (crosspoint1 == crosspoint2){
            crosspoint2 = new Random().nextInt(30) + 1;
        }
        if(crosspoint1 > crosspoint2){
            int container = crosspoint1;
            crosspoint1 = crosspoint2;
            crosspoint2 = container;
        }
        int[] childGenes = new int[32];
        for(int i = 0; i < 32; i++){
            if(i < crosspoint1 || i > crosspoint2){ childGenes[i] = this.genes[i]; }
            else { childGenes[i] = partnersGenes.genes[i]; }
        }
        DNA childDNA = new DNA(childGenes);
        childDNA.mutation();
        return childDNA;
    }

    public void mutation(){
        boolean everyInstance = false;
        while(!everyInstance){
            for(int i = 0; i < 8; i++){
                final int g = i;
                if(IntStream.of(this.genes).noneMatch(x -> x == g)){
                    Random random = new Random();
                    this.genes[random.nextInt(32)] = i;
                    break;
                }
                if(i == 7){
                    everyInstance = true;
                }
            }
        }
        Arrays.sort(this.genes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DNA dna = (DNA) o;
        return Arrays.equals(genes, dna.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }

    @Override
    public String toString() {
        return Arrays.toString(genes);
    }
}
