package proyecto_energias;


public class CalculoCables {

    public  int ntuv = 0, ntuh = 0;
    public int tiposistema, tipoaislamiento;
    float longitud, demax, poct;
    float temperatura;
    float capcom, corrp;
    float I,k,fca,fct,Ipri,s;
    float raiz3 = (float) 1.73205080757;
    String valAwg = "";
    float deltaV=0;

    public float getS() {
        return s;
    }

    public float getCorrp() {
        return corrp;
    }

    public void setCorrp(float corrp) {
        this.corrp = corrp;
    }

    public float getDeltaV() {
        return deltaV;
    }

    public void setDeltaV(float deltaV) {
        this.deltaV = deltaV;
    }

    public float getI() {
        return I;
    }

    public void setI(float I) {
        this.I = I;
    }

    public float getK() {
        return k;
    }

    public void setK(float k) {
        this.k = k;
    }

    public float getFca() {
        return fca;
    }

    public void setFca(float fca) {
        this.fca = fca;
    }

    public float getFct() {
        return fct;
    }

    public void setFct(float fct) {
        this.fct = fct;
    }

    public float getIpri() {
        return Ipri;
    }

    public void setIpri(float Ipri) {
        this.Ipri = Ipri;
    }

    public float getRaiz3() {
        return raiz3;
    }

    public void setRaiz3(float raiz3) {
        this.raiz3 = raiz3;
    }

    public float getCapcom() {
        return capcom;
    }

    public void setCapcom(float capcom) {
        this.capcom = capcom;
    }

    public void setS(float s) {
        this.s = s;
    }

    public String getValAwg() {
        return valAwg;
    }

    public void setValAwg(String valAwg) {
        this.valAwg = valAwg;
    }

    public CalculoCables() {
    }

    public void Longitudmenor40() {
        int[] monofasico = new int[]{18, 23, 31, 42, 54, 74, 100, 132, 163, 198, 252, 305, 353, 400, 456, 536, 617, 738, 848};
        int[] trifasico = new int[]{16, 20, 27, 36, 48, 66, 88, 116, 144, 175, 222, 268, 311, 353, 402, 474, 545, 652, 750};
        double secno[] = new double[]{1, 1.5, 2.5, 4, 6, 10, 16, 25, 35, 50, 70, 95, 120, 150, 185, 240, 300, 400, 500};
        
        float Vcalc = 0;
        if (tiposistema == 120) {
            I = (float) ((demax * 1000) / (1 * tiposistema * 0.9));
        } else {
            I = (float) ((demax * 1000) / (raiz3 * tiposistema * 0.9));
        }
        fca = facoag(getNtuv(), getNtuh());
        fct = facote();
        Ipri = I / (fca * fct);
        if (tipoaislamiento == 1) {
            s = Capconpvc1();
        } else {
            s = Capconepr1();
        }
        deltaV = (tiposistema * poct) / 100;
        k = Calculok();
        Vcalc = k * I * (longitud / 1000);
        if (Vcalc < deltaV) {
            valAwg = calculoawg();
        } else {
            if (tiposistema == 380) {
                for (int i = 0; i < 19; i++) {
                    if (trifasico[i] < k) {
                        Vcalc = k * I * (longitud / 1000);
                        if (Vcalc < deltaV) {
                            s = Calculosecno(trifasico[i], tiposistema);
                            corrp = trifasico[i];
                        } else {
                            i=i+1;
                        }
                    }
                }
            } else {
                for (int i = 0; i < 19; i++) {
                    if (monofasico[i] < k) {
                        Vcalc = k * I * (longitud / 1000);
                        if (Vcalc < deltaV) {
                            s = Calculosecno(monofasico[i], tiposistema);
                            corrp = monofasico[i];
                        } else {
                            i=i+1;
                        }
                    }
                }
            }
            valAwg = calculoawg();
        }

    }

    public void Longitudmayor40() {
        int tri[] = new int[]{16, 20, 27, 36, 48, 66, 88, 116, 144, 175, 222, 268, 311, 353, 402, 474, 545, 652, 750};
        int mono[] = new int[]{18, 23, 31, 42, 54, 74, 100, 132, 163, 198, 252, 305, 353, 400, 456, 536, 617, 738, 848};
        double secno[] = new double[]{1, 1.5, 2.5, 4, 6, 10, 16, 25, 35, 50, 70, 95, 120, 150, 185, 240, 300, 400, 500};
        float resta = 0;
        deltaV = (tiposistema * poct) / 100;
        if (tiposistema == 120) {
            I = (float) ((demax * 1000) / (1 * tiposistema * 0.9));
        } else {
            I = (float) ((demax * 1000) / (raiz3 * tiposistema * 0.9));
        }
        k = deltaV / (I * (longitud / 1000));
        s = Calculosecno(k, tiposistema);
        fca = facoag(getNtuv(), getNtuh());
        fct = facote();
        Ipri = I / (fca * fct);
        if (tipoaislamiento == 1) {
            capcom = Capconpvc();
        } else {
            capcom = Capconepr();
        }
        if (capcom > Ipri) {
            valAwg = calculoawg();
        } else {
            if (tiposistema == 380) {
                for (int i = 0; i < 19; i++) {
                    resta = (float) (tri[i] - Ipri);
                    if (resta >= 0) {
                        s = (float) secno[i];
                        break;
                    }
                }
            } else {
                for (int i = 0; i < 19; i++) {
                    resta = (float) (mono[i] - Ipri);
                    if (resta >= 0) {
                        s = (float) secno[i];
                        break;
                    }
                }
            }
            valAwg = calculoawg();
        }
    }

    public float Calculosecno(float k, int tipsis) {
        double contante = k;
        double resta, sec = 0;
        int sistema = tipsis;
        double tri[] = new double[]{29, 22, 12, 7.5, 5.10, 3, 1.96, 1.28, 0.96, 0.73, 0.54, 0.42, 0.35, 0.31, 0.27, 0.23, 0.20, 0.18, 0.16};
        double mono[] = new double[]{34, 23, 14, 8.7, 5.8, 3.5, 3.31, 1.52, 1.12, 0.82, 0.63, 0.49, 0.41, 0.36, 0.32, 0.26, 0.23, 0.20, 0.19};
        double secno[] = new double[]{1, 1.5, 2.5, 4, 6, 10, 16, 25, 35, 50, 70, 95, 120, 150, 185, 240, 300, 400, 500};

        if (sistema == 380) {
            for (int i = 0; i < 19; i++) {
                resta = contante - tri[i];
                if (resta > 0) {
                    sec = secno[i];
                    break;
                }
            }
        } else {
            for (int i = 0; i < 19; i++) {
                resta = contante - mono[i];
                if (resta > 0) {
                    sec = secno[i];
                    break;
                }
            }
        }
        return (float) sec;
    }

    public String calculoawg() {
        String awga = "";
        double secno[] = new double[]{1, 1.5, 2.5, 4, 6, 10, 16, 25, 35, 50, 70, 95, 120, 150, 185, 240, 300, 400, 500};
        String awg[] = new String[]{"18", "16", "14", "12", "10", "8", "6", "4", "2", "1", "2/0", "3/0", "4/0", "250", "350", "450", "500", "750", "1000"};
        for (int i = 0; i < 19; i++) {
            if (s == secno[i]) {
                awga = awg[i];
            }
        }
        return awga;
    }

    public float Calculok() {
        double constante = 0;
        double tri[] = new double[]{29, 22, 12, 7.5, 5.10, 3, 1.96, 1.28, 0.96, 0.73, 0.54, 0.42, 0.35, 0.31, 0.27, 0.23, 0.20, 0.18, 0.16};
        double mono[] = new double[]{34, 23, 14, 8.7, 5.8, 3.5, 3.31, 1.52, 1.12, 0.82, 0.63, 0.49, 0.41, 0.36, 0.32, 0.26, 0.23, 0.20, 0.19};
        double secno[] = new double[]{1, 1.5, 2.5, 4, 6, 10, 16, 25, 35, 50, 70, 95, 120, 150, 185, 240, 300, 400, 500};

        if (tiposistema == 380) {
            for (int i = 0; i < 19; i++) {
                if (s == secno[i]) {
                    constante = tri[i];
                    break;
                }
            }
        } else {
            for (int i = 0; i < 19; i++) {
                if (s == secno[i]) {
                    constante = mono[i];
                    break;
                }
            }
        }
        return (float) constante;
    }

    public float facoag(int ntv, int nth) {
        int ntver = ntv, nthor = nth;
        double mat[][] = new double[][]{{1, 0.94, 0.91, 0.88, 0.87, 0.86}, {0.92, 0.87, 0.84, 0.81, 0.80, 0.79},
        {0.85, 0.81, 0.78, 0.76, 0.75, 0.74}, {0.82, 0.78, 0.74, 0.73, 0.72, 0.72}, {0.80, 0.76, 0.72, 0.71, 0.70, 0.70},
        {0.79, 0.75, 0.71, 0.70, 0.69, 0.68}};
        return (float) mat[ntver - 1][nthor - 1];
    }

    public float facote() {
        float fcte = 0, resta;
        double pvc[] = new double[]{1.22, 1.17, 1.12, 1.07, 0.93, 0.87, 0.79, 0.71, 0.61, 0.50};
        double epr[] = new double[]{1.15, 1.12, 1.08, 1.04, 0.98, 0.96, 0.94, 0.92, 0.87, 0.84, 0.82, 0.80, 0.72, 0.61};
        double tem[] = new double[]{10, 15, 20, 25, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80};
        if (tipoaislamiento == 1) {
            for (int i = 0; i < 10; i++) {
                resta = (float) (tem[i] - temperatura);
                if (resta >= 0) {
                    fcte = (float) pvc[i];
                    break;
                }
            }
        } else {
            for (int i = 0; i < 14; i++) {
                resta = (float) (tem[i] - temperatura);
                if (resta >= 0) {
                    fcte = (float) epr[i];
                    break;
                }
            }
        }
        return fcte;
    }

    public float Capconepr() {
        float cap = 0;
        int tri[] = new int[]{16, 20, 27, 36, 48, 66, 88, 116, 144, 175, 222, 268, 311, 353, 402, 474, 545, 652, 750};
        int mono[] = new int[]{18, 23, 31, 42, 54, 74, 100, 132, 163, 198, 252, 305, 353, 400, 456, 536, 617, 738, 848};
        double secno[] = new double[]{1, 1.5, 2.5, 4, 6, 10, 16, 25, 35, 50, 70, 95, 120, 150, 185, 240, 300, 400, 500};
        if (tiposistema == 380) {
            for (int i = 0; i < 19; i++) {
                if (s == secno[i]) {
                    cap = tri[i];
                    break;
                }
            }
        } else {
            for (int i = 0; i < 19; i++) {
                if (s == secno[i]) {
                    cap = mono[i];
                    break;
                }
            }
        }
        return cap;
    }

    public float Capconepr1() {
        float sec = 0, resta;
        int tri[] = new int[]{16, 20, 27, 36, 48, 66, 88, 116, 144, 175, 222, 268, 311, 353, 402, 474, 545, 652, 750};
        int mono[] = new int[]{18, 23, 31, 42, 54, 74, 100, 132, 163, 198, 252, 305, 353, 400, 456, 536, 617, 738, 848};
        double secno[] = new double[]{1, 1.5, 2.5, 4, 6, 10, 16, 25, 35, 50, 70, 95, 120, 150, 185, 240, 300, 400, 500};
        if (tiposistema == 380) {
            for (int i = 0; i < 19; i++) {
                resta = (float) (Ipri - tri[i]);
                if (resta <= 0) {
                    sec = (float) secno[i];
                    corrp = tri[i];
                    break;
                }
            }
        } else {
            for (int i = 0; i < 19; i++) {
                resta = (float) (Ipri - mono[i]);
                if (resta <= 0) {
                    sec = (float) secno[i];
                    corrp = mono[i];
                    break;
                }
            }
        }
        return sec;
    }

    public float Capconpvc() {
        float cap = 0;
        double tri[] = new double[]{12, 15.5, 21, 28, 36, 50, 68, 89, 111, 134, 171, 207, 239, 272, 310, 364, 419, 502, 578};
        double mono[] = new double[]{13.5, 17.5, 24, 32, 41, 57, 76, 101, 125, 151, 192, 232, 269, 309, 353, 415, 473, 566, 651};
        double secno[] = new double[]{1, 1.5, 2.5, 4, 6, 10, 16, 25, 35, 50, 70, 95, 120, 150, 185, 240, 300, 400, 500};
        if (tiposistema == 380) {
            for (int i = 0; i < 19; i++) {
                if (s == secno[i]) {
                    cap = (float) tri[i];
                    break;
                }
            }
        } else {
            for (int i = 0; i < 19; i++) {
                if (s == secno[i]) {
                    cap = (float) mono[i];
                    break;
                }
            }
        }
        return cap;
    }

    public float Capconpvc1() {
        float sec = 0, resta;
        double tri[] = new double[]{12, 15.5, 21, 28, 36, 50, 68, 89, 111, 134, 171, 207, 239, 272, 310, 364, 419, 502, 578};
        double mono[] = new double[]{13.5, 17.5, 24, 32, 41, 57, 76, 101, 125, 151, 192, 232, 269, 309, 353, 415, 473, 566, 651};
        double secno[] = new double[]{1, 1.5, 2.5, 4, 6, 10, 16, 25, 35, 50, 70, 95, 120, 150, 185, 240, 300, 400, 500};
        if (tiposistema == 380) {
            for (int i = 0; i < 19; i++) {
                resta = (float) (Ipri - tri[i]);
                if (resta <= 0) {
                    sec = (float) secno[i];
                    corrp = (float) tri[i];
                    break;
                }
            }
        } else {
            for (int i = 0; i < 19; i++) {
                resta = (float) (Ipri - mono[i]);
                if (resta <= 0) {
                    sec = (float) secno[i];
                    corrp = (float) mono[i];
                    break;
                }
            }
        }
        return sec;
    }

    public int getNtuv() {
        return ntuv;
    }

    public void setNtuv(int ntuv) {
        this.ntuv = ntuv;
    }

    public int getNtuh() {
        return ntuh;
    }

    public void setNtuh(int ntuh) {
        this.ntuh = ntuh;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public int getTiposistema() {
        return tiposistema;
    }

    public void setTiposistema(int tiposistema) {
        this.tiposistema = tiposistema;
    }

    public float getDemax() {
        return demax;
    }

    public void setDemax(float demax) {
        this.demax = demax;
    }

    public float getPoct() {
        return poct;
    }

    public void setPoct(float poct) {
        this.poct = poct;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public int getTipoaislamiento() {
        return tipoaislamiento;
    }

    public void setTipoaislamiento(int tipoaislamiento) {
        this.tipoaislamiento = tipoaislamiento;
    }
}
