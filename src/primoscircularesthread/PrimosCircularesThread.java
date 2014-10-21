/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primoscircularesthread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Leonardo Pegorin
 */
public class PrimosCircularesThread {

    private final ArrayList<Integer> primosCirculares = new ArrayList<Integer>();

    public static void main(String[] args) {

        new PrimosCircularesThread().buscar(1000000);

    }

    public void buscar(int nro) {

        int nro4 = nro / 4;
        final ArrayList<BuscarPrimosCircularesThread> hilos = new ArrayList<BuscarPrimosCircularesThread>();

        for (int i = 0; i < 4; i++) {
            hilos.add(new BuscarPrimosCircularesThread(nro4 * i, nro4 * (i + 1)));
        }

        for (BuscarPrimosCircularesThread h : hilos) {
            h.start();
        }

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        synchronized (this) {
                            if (hilos.get(0).isListo() && hilos.get(1).isListo()
                                    && hilos.get(2).isListo() && hilos.get(3).isListo()) {

                                System.out.println("");
                                System.out.println("Se encontraron " + primosCirculares.size() + " primos circulares");
                                Collections.sort(primosCirculares);
                                System.out.println(Arrays.toString(primosCirculares.toArray()));
                                break;
                            }

                            wait(1000);
                        }
                    } catch (InterruptedException ex) {
                        System.out.println("Algo salio mal");
                    }
                }
            }
        });
        thread.start();

    }

    public static Boolean EsPrimo(int n) {
        if (n < 2) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    //Thread para buscar números primos circulares
    public class BuscarPrimosCircularesThread extends Thread {

        private final int inicio;
        private final int fin;
        public boolean termino;

        private BuscarPrimosCircularesThread(int ini, int fin) {
            this.inicio = ini;
            this.fin = fin;
            this.termino = false;
        }

        @Override
        public void run() {

            String cadena, nroCircular = "", copiaNro;

            for (int index = inicio; index <= fin; index++) {
                cadena = Integer.toString(index);
                copiaNro = cadena;

                while (!copiaNro.equals(nroCircular)) {
                    nroCircular = new StringBuilder(cadena).insert(cadena.length(), cadena.charAt(0)).toString();
                    nroCircular = new StringBuilder(nroCircular).delete(0, 1).toString();

                    if (EsPrimo(Integer.parseInt(nroCircular)) == true) {
                        cadena = nroCircular;
                    } else {
                        break;
                    }

                    if (copiaNro.equals(nroCircular)) {
                        synchronized (primosCirculares) {
                            primosCirculares.add(new Integer(cadena));
                        }
                    }
                }
            }

            termino = true;
        }

        public boolean isListo() {
            return termino;
        }
    }


}
