/**
 * Simulateur PVNRT - Groupe B5-2
 */

public class PVNRT {

    // Paramètres de la fenêtre de simulation.
    static final int CHAMBRE_LARGEUR = 100;
    static final int CHAMBRE_HAUTEUR = 100;
    static final int CHAMBRE_HAUTEUR_MIN = 20;
    static final int DELAI = 0;
    static final String WIN_TITRE = "Simulateur PVNRT - Groupe B5-2";
    static final double DELTA_T_SIMULATION = 0.1;
    static final double PISTON_VARIATION_MODULE = 0.1;
    static final int ECHANTILLONS_COURBE = 500;

    // Constantes physiques.
    static final double k = 100;
    static final double kB = 1.38;
    static final double Na = 6.02;
    static final double R = 8.314;

    // Paramètres des particules.
    static final double MASSE_PARTICULE = 30 / Na;
    static final int NOMBRE_PARTICULES = 300;
    static final double VITESSE_INITIALE_PARTICULE = 2;

    // Différentes formules disponibles.
    enum Formule {
        Euler,
        Verlet
    }
    static Formule fMethode;

    // Commentaires.
    static final String COMMENTAIRE_1 = "Algo observé : modèle V1.";
    static final String COMMENTAIRE_2 = "PV=%f   nRT=%f   PV/nRT=%f";

    // Variables globales.
    static double[][] fParticules;
    static int[][] fCouleurs;
    static double fVitesseMax;

    // Variables globales des courbes.
    static double[] fPressions;
    static double[] fTemperatures;
    static double[] fPressionMoyenne;
    static double[] fTemperatureMoyenne;
    static int fIndiceProchainVide;
    static int fIndiceDeb;
    static TraceCourbe fCourbePression;
    static TraceCourbe fCourbeTemperature;
    static TraceCourbe fCourbePressionMoyenne;
    static TraceCourbe fCourbeTemperatureMoyenne;

    /**
     * Initialise la simulation.
     * Crée la fenêtre, avec toutes ses composantes, et l'affiche.
     */
    public static void main(String[] args) {
        // Initialise les particules.
        fParticules = new double[NOMBRE_PARTICULES][6];
        fCouleurs = new int[NOMBRE_PARTICULES][3];
        fVitesseMax = 0;
        for (int i = 0; i < NOMBRE_PARTICULES; i++) {
            fParticules[i] = creerParticule();
            fVitesseMax = Math.max(fVitesseMax, calculVitesseParticule(fParticules[i]));
        }
        for (int i = 0; i < NOMBRE_PARTICULES; i++)
            fCouleurs[i] = creerCouleurParticule(fParticules[i]);

        // Initialiser et afficher la fenêtre.
        Affichage.initAffichage(CHAMBRE_LARGEUR, CHAMBRE_HAUTEUR,
            CHAMBRE_HAUTEUR_MIN, DELAI, WIN_TITRE, DELTA_T_SIMULATION,
            PISTON_VARIATION_MODULE);
        Affichage.setParticules(fParticules, fCouleurs);
        Affichage.setCommentaire(COMMENTAIRE_1);

        // Ajouter les courbes.
        fPressions = new double[ECHANTILLONS_COURBE];
        fTemperatures = new double[ECHANTILLONS_COURBE];
        fPressionMoyenne = new double[ECHANTILLONS_COURBE];
        fTemperatureMoyenne = new double[ECHANTILLONS_COURBE];
        fIndiceProchainVide = fIndiceDeb = 0;
        fCourbePression = TraceCourbe.getCourbe("Pression instantanée");
        fCourbeTemperature = TraceCourbe.getCourbe("Température instantanée");
        fCourbePressionMoyenne = TraceCourbe.getCourbe("Pression moyenne");
        fCourbeTemperatureMoyenne = TraceCourbe.getCourbe("Température moyenne");
        Affichage.addCourbe(fCourbePression);
        Affichage.addCourbe(fCourbeTemperature);
        Affichage.addCourbe(fCourbePressionMoyenne);
        Affichage.addCourbe(fCourbeTemperatureMoyenne);
    }

    /**
     * Renvoie une nouvelle particule.
     * Génère aléatoirement une position et une vitesse initiale.
     * La norme de la vitesse et l'angle initial sont choisis
     * aléatoirement.
     */
    public static double[] creerParticule() {
        double vitesse = VITESSE_INITIALE_PARTICULE * Math.random();
        double theta = Math.random() * 2 * Math.PI;
        double[] particule = {
            randomInRange(0, CHAMBRE_LARGEUR),             // Position X
            randomInRange(0, CHAMBRE_HAUTEUR_MIN),         // Position Y
            vitesse * Math.cos(theta),                     // Vitesse X
            vitesse * Math.sin(theta),                     // Vitesse Y
            0,                                             // Accélération X
            0                                              // Accélération Y
        };
        return particule;
    }

    /**
     * Renvoie un tableau RGB de couleur représentatif de la vitesse.
     */
    public static int[] creerCouleurParticule(double[] particule) {
        double rapport = calculVitesseParticule(particule) / fVitesseMax;
        int[] couleur = {
            (rapport > 0.6) ? (int) (rapport * 255) : 0,
            (rapport >= 0.3 && rapport < 0.6) ? (int) (rapport * 255) : 0,
            (rapport < 0.3) ? (int) ((1 - rapport) * 255) : 0
        };
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
     * Renvoie les composantes de la force appliquée sur une particule.
     */
    public static double[] calculForceParticule(double[] particule) {
        double[] force = {0.0, 0.0};

        // Haut
        if (particule[1] < 0)
            force[1] = -k * particule[1];

        // Bas
        else if (particule[1] > Affichage.getPiston())
            force[1] = -k * (particule[1] - Affichage.getPiston());

        // Gauche
        if (particule[0] < 0)
            force[0] = -k * particule[0];

        // Droite
        else if (particule[0] > CHAMBRE_LARGEUR)
            force[0] = -k * (particule[0] - CHAMBRE_LARGEUR);

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
        double sommeForce = 0.0;
        double pressionMoyenne = 0.0;
        double surfaceTotale = 0.0;
        for (int i = 0; i < fParticules.length; i++)
            sommeForce += norme(calculForceParticule(fParticules[i]));
        surfaceTotale = 2 * CHAMBRE_LARGEUR + 2 * Affichage.getPiston();
        pressionMoyenne = sommeForce / surfaceTotale;
        
        return pressionMoyenne;
    }

    /**
     * Renvoie la cumulation des pressions moyennes calculées jusqu'ici (Pa).
     */
    public static double calculPressionMoyenneCumulees() {
        double somme = 0;
        double non_vides = 0;
        for (int i = 0; i < fPressions.length; i++) {
            somme += fPressions[i];
            if (fPressions[i] != 0)
                non_vides += 1;
        }
        return somme / non_vides;
    }

    /**
     * Renvoie la température moyenne instantanée dans la chambre (K).
     * La température est calculée avec la formule du premier TD.
     */
    public static double calculTemperatureMoyenneInstantanee() {
        double temperature = 0.0;
        for(int i = 0; i < fParticules.length; i++)
            temperature += Math.pow(calculVitesseParticule(fParticules [i]), 2);
        temperature *= MASSE_PARTICULE / (3 * kB);
        return temperature;
    }

    /**
     * Renvoie la cumulation des températures moyennes calculées jusqu'ici (K).
     */
    public static double calculTemperatureMoyenneCumulees() {
        double somme = 0;
        double non_vides = 0;
        for (int i = 0; i < fTemperatures.length; i++) {
            somme += fTemperatures[i];
            if (fTemperatures[i] != 0)
                non_vides += 1;
        }
        return somme / non_vides;
    }

    /**
     * Renvoie le produit PV.
     */
    public static double calculPV() {
        return calculPressionMoyenneCumulees() * CHAMBRE_LARGEUR * Affichage.getPiston();
    }

    /**
     * Renvoie le produit nRT.
     */
    public static double calculnRT() {
        return NOMBRE_PARTICULES / Na * R * calculTemperatureMoyenneCumulees();
    }

    /**
     * Actualise la position de la particule.
     */
    public static void actualiserPositionParticule(int i) {
        double[] p = fParticules[i];
        p[0] += p[2] * DELTA_T_SIMULATION;
        p[1] += p[3] * DELTA_T_SIMULATION;
    }

    /**
     * Actualise la vitesse de la particule.
     */
    public static void actualiserVitesseParticule(int i) {
        double[] p = fParticules[i];
        actualiserAccelerationParticule(i);
        p[2] += p[4] * DELTA_T_SIMULATION;
        p[3] += p[5] * DELTA_T_SIMULATION;
        fVitesseMax = Math.max(fVitesseMax, calculVitesseParticule(fParticules[i]));
    }

    /**
     * Actualise l'accélération de la particule en fonction des forces.
     */
    public static void actualiserAccelerationParticule(int i) {
        double[] p = fParticules[i];
        double[] force = calculForceParticule(p);
        p[4] = force[0] / MASSE_PARTICULE;
        p[5] = force[1] / MASSE_PARTICULE;
    }

    /**
     * Appelé lorsque la fenêtre rafraîchit l'affichage.
     * Déplace les particules et actualise les graphiques.
     */
    public static void next() {
        // Déterminer la méthode en cours d'utilisation.
        fMethode = Affichage.getCalcXYmethod().equals("EULER") ?
            Formule.Euler : Formule.Verlet;

        // Mise à jour des positions et des vitesses des particules.
        for (int i = 0; i < NOMBRE_PARTICULES; i++) {
            if (fMethode == Formule.Euler) {
                actualiserPositionParticule(i);
                actualiserVitesseParticule(i);
            } else if (fMethode == Formule.Verlet) {
                actualiserVitesseParticule(i);
                actualiserPositionParticule(i);
            }
            fCouleurs[i] = creerCouleurParticule(fParticules[i]);
        }

        // Mise à jour des courbes.
        fPressions[fIndiceProchainVide] = calculPressionMoyenneInstantanee();
        fTemperatures[fIndiceProchainVide] = calculTemperatureMoyenneInstantanee();
        fPressionMoyenne[fIndiceProchainVide] = calculPressionMoyenneCumulees();
        fTemperatureMoyenne[fIndiceProchainVide] = calculTemperatureMoyenneCumulees();
        fIndiceProchainVide  = (fIndiceProchainVide + 1) % fPressions.length;
        if (fIndiceProchainVide == fIndiceDeb)
            fIndiceDeb = (fIndiceDeb + 1) % fPressions.length;
        fCourbePression.updtData(fPressions, fIndiceDeb, fIndiceProchainVide);
        fCourbeTemperature.updtData(fTemperatures, fIndiceDeb, fIndiceProchainVide);
        fCourbePressionMoyenne.updtData(fPressionMoyenne, fIndiceDeb, fIndiceProchainVide);
        fCourbeTemperatureMoyenne.updtData(fTemperatureMoyenne, fIndiceDeb, fIndiceProchainVide);

        // Afficher les valeurs de PV et nRT.
        Affichage.setCommentaire(String.format(COMMENTAIRE_2, calculPV(), calculnRT(), calculPV()/calculnRT()));
    }

    /**
     * Calcule un int aléatoire entre deux valeurs minimales et maximales.
     */
    public static int randomInRange(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    /**
     * Renvoie la norme d'une force.
     */
    public static double norme(double[] force) {
        return Math.sqrt(Math.pow(force[0], 2) + Math.pow(force[1], 2));
    }

}
