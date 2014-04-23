/**
 * Simulateur PVNRT - Groupe B5-2
 */

import java.math.*;

public class PVNRT {

    // Paramètres des particules.
    static final double MASSE_PARTICULE = 40.0;
    static final int NOMBRE_PARTICULES = 100;
    static final double VITESSE_INITIALE_PARTICULE = 1.0;

    // Paramètres de la fenêtre de simulation.
    static final int CHAMBRE_LARGEUR = 100;
    static final int CHAMBRE_HAUTEUR = 100;
    static final int HAUTEUR_MIN = 20;
    static final int DELAI = 10;
    static final String WIN_TITRE = "Simulateur PVNRT - Groupe B5-2";
    static final double DELTA_T_SIMULATION = 0.2;
    static final double PISTON_VARIATION_MODULE = 0.1;

    // Constantes physiques.
    static final BigDecimal kB = new BigDecimal("1.38E-16");
    static final double R = 8.314;

    // Variables globales.
    static double[][] fParticules;
    static int[][] fCouleurs;

    /**
     * Initialise la simulation.
     * Crée la fenêtre, avec toutes ses composantes, et l'affiche.
     */
    public static void main(String[] args) {
        // Initialise les particules.
        fParticules = new double[NOMBRE_PARTICULES][6];
        fCouleurs = new int[NOMBRE_PARTICULES][3];
        double vitesse_max = 0;
        for (int i = 0; i < NOMBRE_PARTICULES; i++) {
            fParticules[i] = creerParticule();
            vitesse_max = Math.max(vitesse_max, calculVitesseParticule(fParticules[i]));
        }
        for (int i = 0; i < NOMBRE_PARTICULES; i++)
            fCouleurs[i] = creerCouleurParticule(fParticules[i], vitesse_max);

        // Initialiser et afficher la fenêtre.
	    Affichage.initAffichage(CHAMBRE_LARGEUR, CHAMBRE_HAUTEUR, HAUTEUR_MIN,
	        DELAI, WIN_TITRE, DELTA_T_SIMULATION, PISTON_VARIATION_MODULE);
        Affichage.setParticules(fParticules, fCouleurs);
    }

    /**
     * Renvoie une nouvelle particule.
     * Génère aléatoirement une position et une vitesse initiale.
     * La norme de la vitesse est toujours la même, mais l'angle initial est
     * choisi aléatoirement dans [0; 2π[.
     */
    public static double[] creerParticule() {
        double theta = Math.random() * 2 * Math.PI;
        double[] particule = {
            randomInRange(0, CHAMBRE_LARGEUR),
            randomInRange(0, HAUTEUR_MIN),
            VITESSE_INITIALE_PARTICULE * Math.cos(theta),
            VITESSE_INITIALE_PARTICULE * Math.sin(theta),
            0,
            0};
        return particule;
    }

    /**
     * Renvoie un tableau RGB de couleur représentatif de la vitesse.
     */
    public static int[] creerCouleurParticule(double[] particule, double vitesse_max) {
        double rapport = calculVitesseParticule(particule) / vitesse_max;
        int[] couleur = {(int) (rapport * 255), 0, 0};
        return couleur;
    }

    /**
     * Renvoie la norme de la vitesse d'une particule (m.s^-1).
     */
    public static double calculVitesseParticule(double[] particule) {
        double vitesse = Math.sqrt(Math.pow(particule[2], 2) + Math.pow(particule[3], 2));
        return vitesse;
    }

    /**
     * Renvoie la norme de la force appliquée sur une particule (N).
     */
    public static double[] calculForceParticule(double[] particule) {
        double[] force = {0.0, 0.0};
        return force;
    }

    /**
     * Renvoie la pression moyenne instantanée dans la chambre (Pa).
     * Pour calculer la pression instantanée, la moyenne des pressions
     * exercées sur chacun des 4 murs est calculée, elles-mêmes calculées en
     * sommant les forces exercées par chaque particule sur chaque mur et en
     * les divisant par la surface (la longueur du côté).
     */
    public static double calculPressionMoyenneInstantanee() {
        return 0.0;
    }

    /**
     * Renvoie la cumulation des pressions moyennes calculées jusqu'ici (Pa).
     */
    public static double calculPressionMoyenneCumulees() {
        return 0.0;
    }

    /**
     * Renvoie la température moyenne instantanée dans la chambre (K).
     * La température est calculée en effectuant la somme de l'énergie
     * cinétique de chaque particule (calculée grâce à la vitesse instantanée
     * de chacune avec Ec = 1/2 * m * v^2).
     */
    public static double calculTemperatureMoyenneInstantanee() {
        return 0.0;
    }

    /**
     * Renvoie la cumulation des températures moyennes calculées jusqu'ici (K).
     */
    public static double calculTemperatureMoyenneCumulees() {
        return 0.0;
    }

    /**
     * Renvoie le produit P*V.
     */
    public static double calculPV() {
        return 0.0;
    }

    /**
     * Renvoie le produit nRT.
     */
    public static double calculnRT() {
        return 0.0;
    }

    /**
     * Appelé lorsque la fenêtre rafraîchit l'affichage.
     * Déplace les particules et actualise les graphiques.
     */
    public static void next() {
        
    }

    /**
     * Calcule un int aléatoire entre deux valeurs minimales et maximales.
     */
    public static int randomInRange(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

}
