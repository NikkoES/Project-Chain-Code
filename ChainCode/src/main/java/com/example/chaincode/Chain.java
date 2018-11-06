package com.example.chaincode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

public class Chain {

    // image properties -- properti gambar
    int h; // height
    int w; // width

    // shape properties -- properti objek didalam gambar / benda dalam gambar
    double height;
    double width;

    // bitmaps -- satuan gambar
    int pixels[][]; // image with treshold 1 or 0
    int visited[][]; // stores visited pixels

    // initial coordinates of the shape -- inisiasi titik coordinat dari objek/benda
    int begin[];

    // final coordinates of the shape -- titik akhir coordinat dari objek/benda
    int end[];

    // perimeter -- keliling dari objek
    int points;
    double perimeter;
    double area;

    public Chain(String filename) throws IOException {

        // read input file -- membaca inputan yang kita masukan hanya berupa png.
        //System.out.println();
        //System.out.print("Filename: ");
        //filename = Input.readString();
        File shape = new File(filename);
        BufferedImage image = ImageIO.read(shape);


        // set image properties for later use -- menentukan properti gambar(bukan objek)
        h = image.getHeight(); // height
        w = image.getWidth(); // width

        // initialize coordinates vectors -- memulai proses penentuan coordinat vector
        begin = new int[2];
        end = new int[2];
        points = 0;
        perimeter = 0;
        area = 0;

        // treshold image - untuk mengubah gambar jadi hitamputih
        pixels = new int[h][w];
        visited = new int [h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixels[i][j] = image.getRGB(j, i);
                if (pixels[i][j] != -1) {
                    // shades of gray -> black -- ini merubah yang objek abu jadi hitam objek jelas
                    pixels[i][j] = 1;
                } else {
                    // background -> white -- merubah yg bukan objek jadi putih atau background
                    pixels[i][j] = 0;
                }
                // set pixel as unvisited -- penanda pixel yang sudah dicek atau deteksi
                visited[i][j] = 0;
            }
        }
    }

    public void firstPixel() {
        boolean flag = false;
        // locate first black pixel -- menandai titik hitam pixel pertama
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (pixels[i][j] == 1 && !(flag)) {
                    // get coordinates
                    begin[0] = i;
                    begin[1] = j;
                    flag = true;
                }
            }
        }
    }

    public void lastPixel() {
        boolean flag = false;
        // find first pixel from down-up -- menemukan titik pixel pertama dari bawah ke atas
        for (int i = h - 1; i >= 0; i--) {
            for (int j = w - 1; j >= 0; j--) {
                if (pixels[i][j] == 1 && !(flag)) {
                    // get coordinates
                    end[0] = i;
                    end[1] = j;
                    flag = true;
                }
            }
        }
    }

    public void setHeight() {
        // y of last pixel - y of first pixel -- menentukan tinggi si objek
        height = (end[0] - begin[0] + 1);
    }

    public void setWidth() {

        // get x coordinates of first and final pixels -- menentukan lebar si objek
        int aux[] = new int[2];
        boolean flag = false;
        // find first pixel to the left -- cari pixel dari objek sisi kiri
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (pixels[j][i] == 1 && !(flag)) {
                    // get x coordinate
                    aux[0] = i;
                    flag = true;
                }
            }
        }

        flag = false;
        // find first pixel to the right --cari pixel dari objek sisi kanan
        for (int i = w - 1; i >= 0; i--) {
            for (int j = h - 1; j >= 0; j--) {
                if (pixels[j][i] == 1 && !(flag)) {
                    // get x coordinate
                    aux[1] = i;
                    flag = true;
                }
            }
        }

        // x of last pixel - x of first pixel -- untuk mendapatkan lebar si objek maka x sisi kanan di kurang x sisi kiri
        width = (aux[1] - aux[0] + 1); // dibagi 37,795 karena 1cm = 37,795pixels

    }

    public void border() {

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (pixels[i][j] == 1) {
        			/* if a neighbor of a pixel is empty, that pixel
                    is on the border of the shape

                    -- misalnya pixel disamping objek yg berwarna hitam
                    warnanya bukan hitam(putih seperti bakground) berarti
                    si pixel paling ujung dari titik hitam terakhir
                    menjadi batas pixel  */

                    if (borderPixel(i, j)) points++;
                }
            }
        }
    }

    public boolean borderPixel(int i, int j) {

        // only check black pixels -- untuk mengecek pixel yang hitam, selain itu diabaikan
        if (pixels[i][j] == 0) return false;

        // cek batasan objek untuk 4 arah
        // check left -- kiri
        if (j == 0) return true; // image border = shape border
        if (j > 0) {
            if (pixels[i][j - 1] == 0) {
                return true;
            }
        }

        // check up -- atas
        if (i == 0) return true;
        if (i > 0) {
            if (pixels[i - 1][j] == 0) {
                return true;
            }
        }

        // check right -- kanan
        if (j == w) return true;
        if (j < w) {
            if (pixels[i][j + 1] == 0) {
                return true;
            }
        }

        // check down -- bawah
        if (i == h) return true;
        if (i < h) {
            if (pixels[i + 1][j] == 0) {
                return true;
            }
        }

        // no empty pixel around = not border pixel -- jika tidak ada pixel putih(kosong) maka border tidak akan terdeteksi
        return false;
    }

    public int[] borderNeighbors(int i, int j) {

        int index[] = new int[2];
        boolean flag = false;

        // check around pixel for unvisited border pixels -- memeriksa piksel disekitar objek yang tidak terdeteksi bordernya
        // calculates chain codes distance -- mulai masuk ke rumus chaincodes

        // check east -- kode rantai 0
        if (borderPixel(i, j+1) && !flag && !flag && visited[i][j+1] == 0) {
            j = j + 1;
            System.out.print("0 ");
            perimeter += 1; // keliling 1cm = 37.795pixels
            area += -i;
        /* luas itu 0 dan y nya itu i, kalo x itu j
           supaya jadi cm maka di akhir harus di dibagi
           1cm2 = 37.795^2 pixels kuadrat */
            flag = true;
            index[0] = i; // i adalah y di fungsi matematik
            index[1] = j; // j adalah x di fungsi matematik
            return index;
        }
        // check southeast
        if (borderPixel(i+1, j+1) && !flag && visited[i+1][j+1] == 0) {
            i = i + 1;
            j = j + 1;
            System.out.print("7 ");
            perimeter += Math.sqrt(2); // keliling 1cm = 37.795pixels
            area += -(i + 0.5);
        /* luas itu 0 dan y nya itu i, kalo x itu j
           supaya jadi cm maka di akhir harus di dibagi
          1cm2 = 37.795^2 pixels kuadrat */
            flag = true;
            index[0] = i; // i adalah y di fungsi matematik
            index[1] = j; // j adalah x di fungsi matematik
            return index;
        }
        // check south
        if (borderPixel(i+1, j) && !flag && visited[i+1][j] == 0) {
            i = i + 1;
            System.out.print("6 ");
            perimeter += 1; // keliling 1cm = 37.795pixels
            area += 0;
        /* luas itu 0 dan y nya itu i, kalo x itu j
          supaya jadi cm maka di akhir harus di dibagi
          1cm2 = 37.795^2 pixels kuadrat */
            flag = true;
            index[0] = i; // i adalah y di fungsi matematik
            index[1] = j; // j adalah x di fungsi matematik
            return index;
        }
        // check southwest
        if (borderPixel(i+1, j-1) && !flag && visited[i+1][j-1] == 0) {
            i = i + 1;
            j = j - 1;
            System.out.print("5 ");
            perimeter += Math.sqrt(2); // keliling 1cm = 37.795pixels
            area += (i + 0.5);
        /* luas itu 0 dan y nya itu i, kalo x itu j
           supaya jadi cm maka di akhir harus di dibagi
           1cm2 = 37.795^2 pixels kuadrat */
            flag = true;
            index[0] = i; // i adalah y di fungsi matematik
            index[1] = j; // j adalah x di fungsi matematik
            return index;
        }
        // check west
        if (borderPixel(i, j-1) && !flag && visited[i][j-1] == 0) {
            j = j - 1;
            System.out.print("4 ");
            perimeter += 1; // keliling 1cm = 37.795pixels
            area += i;
        /* luas itu 0 dan y nya itu i, kalo x itu j
          supaya jadi cm maka di akhir harus di dibagi
          1cm2 = 37.795^2 pixels kuadrat */
            flag = true;
            index[0] = i; // i adalah y di fungsi matematik
            index[1] = j; // j adalah x di fungsi matematik
            return index;
        }
        // check northwest
        if (borderPixel(i-1, j-1) && !flag && visited[i-1][j-1] == 0) {
            i = i - 1;
            j = j - 1;
            System.out.print("3 ");
            perimeter += Math.sqrt(2); // keliling 1cm = 37.795pixels
            area += (i - 0.5);
        /* luas itu 0 dan y nya itu i, kalo x itu j
            supaya jadi cm maka di akhir harus di dibagi
              1cm2 = 37.795^2 pixels kuadrat */
            flag = true;
            index[0] = i; // i adalah y di fungsi matematik
            index[1] = j; // j adalah x di fungsi matematik
            return index;
        }
        // check north
        if (borderPixel(i-1, j) && !flag && visited[i-1][j] == 0) {
            i = i - 1;
            System.out.print("2 ");
            perimeter += 1; // keliling 1cm = 37.795pixels
            area += 0;
        /* luas itu 0 dan y nya itu i, kalo x itu j
            supaya jadi cm maka di akhir harus di dibagi
              1cm2 = 37.795^2 pixels kuadrat */
            flag = true;
            index[0] = i; // i adalah y di fungsi matematik
            index[1] = j; // j adalah x di fungsi matematik
            return index;
        }
        // check northeast
        if (borderPixel(i-1, j+1) && !flag && visited[i-1][j+1] == 0) {
            i = i - 1;
            j = j + 1;
            System.out.print("1 ");
            perimeter += Math.sqrt(2); // keliling 1cm = 37.795pixels
            area += -(i - 0.5);
        /* luas itu 0 dan y nya itu i, kalo x itu j
            supaya jadi cm maka di akhir harus di dibagi
              1cm2 = 37.795^2 pixels kuadrat */
            flag = true;
            index[0] = i; // i adalah y di fungsi matematik
            index[1] = j; // j adalah x di fungsi matematik
            return index;
        }
        // no neighbor border pixels
        index[0] = i;
        index[1] = j;
        return index;
    }

    public void chainCodes(int i, int j) {
    	/*
    	i e j = index of current pixel
    	index[0], index[1] = next border pixel (if exists)
    	*/

        // coordinates of current pixel
        int[] index = new int[2];

        // check for border pixels around
        index = borderNeighbors(i, j);

        // set pixel as visited
        visited[i][j] = 1;

        // if next border pixel is visited, we're back to the first pixel
        if (visited[index[0]][index[1]] == 0) {
            chainCodes(index[0], index[1]);
        } else {
            System.out.println();
        }
    }

    public static void main(String gamvar) throws IOException {
        String gambar = gamvar;

        Chain c = new Chain(gambar);

        // get key coordinates
        c.firstPixel();
        c.lastPixel();

        // generate chain codes
        // get coordinates of first border pixel after initial
        int[] index = new int[2];
        System.out.println();
        System.out.print("Chain Codes: ");
        index = c.borderNeighbors(c.begin[0], c.begin[1]);
        c.chainCodes(index[0], index[1]);
        System.out.println();

        // calculate shape properties
        c.setHeight();
        c.setWidth();

        // mengubah angka dibelakang koma menjadi 2 angka saja
        DecimalFormat df = new DecimalFormat("0.000");
        DecimalFormat df2 = new DecimalFormat("0");

        //get all pixels size
        System.out.println("SATUAN PIXEL :");
        System.out.println();
        System.out.println("Shape width: " + df2.format(c.width) +" pixels");
        System.out.println("Shape height: " + df2.format(c.height) +" pixels");
        System.out.println("Shape perimeter: " + df.format(c.perimeter) + " pixels");
        System.out.println("Shape area: "+ df.format(c.area) + " pixels");

        System.out.println();
        //get all cm size
        System.out.println("SATUAN CENTIMETER :");
        System.out.println();
        System.out.println("Shape width: " + df.format(c.width/37.795) +" cm");
        System.out.println("Shape height: " + df.format(c.height/37.795) +" cm");
        System.out.println("Shape perimeter: " + df.format(c.perimeter/37.795) + " cm");
        System.out.println("Shape area: "+ df.format(c.area/Math.pow(37.795, 2)) + " cm");

        //get border shape
        c.border();
        System.out.println();
        System.out.println("Border pixels: " + c.points + " pixels");
    }
}
