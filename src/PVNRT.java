/**
 * Simulateur PVNRT - Groupe B5-2
 */

public class PVNRT {

    static final int CHAMBRE_WIDTH = 100;
    static final int CHAMBRE_HEIGHT = 100;
    static final int LARGEUR_MIN = 20;
    static final int DELAY = 10;
    static final String WIN_TITRE = "Simulateur PVNRT - Groupe B5-2";
    static final double DELTA_T_SIMULATION = 0.2;
    static final double PISTON_VARIATION_MODULE = 0.1;

    public static void main(String[] args) {
        // Initialiser et afficher la fenêtre.
	    Affichage.initAffichage(CHAMBRE_WIDTH, CHAMBRE_HEIGHT, LARGEUR_MIN,
	        DELAY, WIN_TITRE, DELTA_T_SIMULATION, PISTON_VARIATION_MODULE);

        // Afficher deux particules.
        double[][] particules = {{10, 10, 10, 10}, {20, 20, 20, 20}};
        int[][] couleurs = {{93, 230, 230}, {248, 24, 92}};
        Affichage.setParticules(particules, couleurs);

        // Afficher une courbe.
        TraceCourbe courbeTest = TraceCourbe.getCourbe("TEST");
        Affichage.addCourbe(courbeTest);
    }

    public static void next(){
        
    }


}
