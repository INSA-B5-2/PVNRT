/**
 * Simulateur PVNRT - Groupe B5-2
 */

import java.math.*;

public class PVNRT {

    // Paramètres des particules.
    static final double MASSE_PARTICULE = 1.0;
    static final double NOMBRE_PARTICULES = 100;
    static final double VITESSE_INITIALE_PARTICULE = 1.0;

    // Paramètres de la fenêtre de simulation.
    static final int CHAMBRE_WIDTH = 100;
    static final int CHAMBRE_HEIGHT = 100;
    static final int LARGEUR_MIN = 20;
    static final int DELAI = 10;
    static final String WIN_TITRE = "Simulateur PVNRT - Groupe B5-2";
    static final double DELTA_T_SIMULATION = 0.2;
    static final double PISTON_VARIATION_MODULE = 0.1;

    // Constantes physiques.
    static final BigDecimal kB = new BigDecimal("1.38E-16");
    static final double R = 8.314;

    // Différentes formules disponibles.
    enum Formule {
        Euler,
        Verlet
    }

    // Variables globales.
    static double[][] fParticules;
    static Formule fFormule = Formule.Euler;

    /**
     * Initialise la simulation.
     * Crée la fenêtre, avec toutes ses composantes, et l'affiche.
     */
    public static void main(String[] args) {
        // Initialise les particules.

        // Initialiser et afficher la fenêtre.
	    Affichage.initAffichage(CHAMBRE_WIDTH, CHAMBRE_HEIGHT, LARGEUR_MIN,
	        DELAI, WIN_TITRE, DELTA_T_SIMULATION, PISTON_VARIATION_MODULE);
    }

    /**
     * Renvoie une nouvelle particule.
     * Génère aléatoirement une position et une vitesse initiale.
     * La norme de la vitesse est toujours la même, mais l'angle initial est
     * choisi aléatoirement dans [0; 2π[.
     */
    public static double[] creerParticule() {
        double[] particule = {0, 0, 0, 0};
        return particule;
    }

    /**
     * Renvoie la norme de la force appliquée sur une particule (N).
     */
    public static double calculForceParticule() {
        return 0.0;
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
    public static void next(){
        
    }


}
