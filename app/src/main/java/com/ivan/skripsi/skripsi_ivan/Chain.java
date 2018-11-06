package com.ivan.skripsi.skripsi_ivan;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.chaincode.Input;

import java.io.IOException;
import java.text.DecimalFormat;

public class Chain {

    // image properties -- properti dari gambar
    int h; // height
    int w; // width


    // shape properties -- properti objek didalam gambar / benda dalam gambar
    double height;
    double width;

    // bitmaps -- satuan gambar

    /*
        memisahkan antara objek dengan background
        dalam suatu citra berdasarkan pada perbedaan
        tingkat kecerahannya atau gelap terang nya
    */
    int pixels[][]; // image with treshold 1 or 0 -- gambar dengan segmen pemisah 1 atau 0
    int visited[][]; // stores visited pixels -- menyimpan pixel yang sudah terpisahkan / teridentifikasi

    int pixels_btm[][]; // image with treshold 1 or 0 -- gambar dengan segmen pemisah 1 atau 0
    int visited_btm[][]; // stores visited pixels -- menyimpan pixel yang sudah terpisahkan / teridentifikasi

    // initial coordinates of the shape -- inisiasi titik awal coordinat dari objek/benda
    int begin[];

    // final coordinates of the shape -- titik akhir coordinat dari objek/benda
    int end[];

    // perimeter & area-- keliling & luas dari objek
    int points;
    double perimeter;
    double area;

    String filename = new String(); //deklarasi variable filename

    Bitmap image;

    public Chain(Bitmap bitmap) {

        image = bitmap;

        // read input file -- membaca inputan yang kita masukan berupa .png atau .jpg
        System.out.println();
        System.out.print("Filename: ");
//        filename = Input.readString(); //mengambil nama file yang kita inputkn
////        File shape = new File(filename);

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
        visited = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixels[i][j] = image.getPixel(j, i);
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

        pixels_btm = new int[h][w];
        visited_btm = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixels_btm[i][j] = image.getPixel(j, i);
                if (pixels_btm[i][j] != -1) {
                    // shades of gray -> black -- ini merubah yang objek abu jadi hitam objek jelas
                    pixels_btm[i][j] = 1;
                } else {
                    // background -> white -- merubah yg bukan objek jadi putih atau background
                    pixels_btm[i][j] = 0;
                }
                // set pixel as unvisited -- penanda pixel yang sudah dicek atau deteksi
                visited_btm[i][j] = 0;
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
                    System.out.println("Nilai i firstPixel : " + i);
                }
            }
        }
    }

    //first pixel dan last pixel 1 itu diatur sampai setengah dari image bukan dari Shape
// first pixel 2 dan last pixel 2 dimulai dari akhir si pixel 1
// untuk bisa scanning 2 objek sama nampilin chaincode
    public void firstPixel2() {
        boolean flag = false;
        // locate first black pixel -- menandai titik hitam pixel pertama
        for (int i = h / 2; i < h; i++) {
            for (int j = w / 2; j < w; j++) {
                if (pixels_btm[i][j] == 1 && !(flag)) {
                    // get coordinates
                    begin[0] = i;
                    begin[1] = j;
                    flag = true;
                    System.out.println("Nilai i firstPixel 2 : " + i);
                }
            }
        }
    }

    public void lastPixel() {
        boolean flag = false;
        // find first pixel from down-up -- menemukan titik pixel pertama dari bawah ke atas
        for (int i = h / 2 - 1; i >= 0; i--) {
            for (int j = w / 2 - 1; j >= 0; j--) {
                if (pixels[i][j] == 1 && !(flag)) {
                    // get coordinates
                    end[0] = i;
                    end[1] = j;
                    flag = true;
                    System.out.println("Nilai i endPixel : " + i);
                }
            }
        }
    }

    public void lastPixel2() {
        boolean flag = false;
        // find first pixel from down-up -- menemukan titik pixel pertama dari bawah ke atas
        for (int i = h - 1; i >= 0; i--) {
            for (int j = w - 1; j >= 0; j--) {
                if (pixels_btm[i][j] == 1 && !(flag)) {
                    // get coordinates
                    end[0] = i;
                    end[1] = j;
                    flag = true;
                    System.out.println("Nilai i endPixel : " + i);
                }
            }
        }
    }

    //kalo set height 1 dan 2 untuk manggil nilai setelah scanning 2 objek

    public void setHeight() {
        // y of last pixel - y of first pixel -- menentukan tinggi si objek
        height = (end[0] - begin[0] + 1);
    }

    public void setHeight2() {
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

    public void setWidth2() {

        // get x coordinates of first and final pixels -- menentukan lebar si objek
        int aux[] = new int[2];
        boolean flag = false;
        // find first pixel to the left -- cari pixel dari objek sisi kiri
        for (int i = h / 2; i < w; i++) {
            for (int j = w / 2; j < h; j++) {
                if (pixels_btm[j][i] == 1 && !(flag)) {
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
                if (pixels_btm[j][i] == 1 && !(flag)) {
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
        int i_from = 0;
        int j_from = 0;
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
                    //System.out.print("Nilai i = " + i);
                    //System.out.print("Nilai j = " + j);
                    i_from = i; // i = y
                    j_from = j; // j = x
                }
            }
        }
    }

    public void border2() {
        int i_from_btm = 0;
        int j_from_btm = 0;
        //tambahan dari Nanda
        for (int y = h; y < 0; y--) {
            for (int z = w; z < 0; z--) {
                if (pixels_btm[y][z] == 1) {
              /* if a neighbor of a pixel is empty, that pixel
                    is on the border of the shape

                    -- misalnya pixel disamping objek yg berwarna hitam
                    warnanya bukan hitam(putih seperti bakground) berarti
                    si pixel paling ujung dari titik hitam terakhir
                    menjadi batas pixel  */

                    if (borderPixel2(y, z)) points++;
                    //System.out.print("Nilai i = " + i);
                    //System.out.print("Nilai j = " + j);
                    i_from_btm = y; // i = y
                    j_from_btm = z; // j = x
                }
            }
        }
    }

    //border pixel mengecek dari 4 arah bordernya
    public boolean borderPixel(int i, int j) {
        // i adalah y di fungsi matematika
        // j adalah x di fungsi matematika

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

    //border pixel mengecek dari 4 arah bordernya
    public boolean borderPixel2(int i, int j) {
        // i adalah y di fungsi matematika
        // j adalah x di fungsi matematika

        // only check black pixels -- untuk mengecek pixel yang hitam, selain itu diabaikan
        if (pixels_btm[i][j] == 0) return false;

        // cek batasan objek untuk 4 arah
        // check left -- kiri

        if (j == 0) return true; // image border = shape border
        if (j > 0) {
            if (pixels_btm[i][j - 1] == 0) {
                return true;
            }
        }

        // check up -- atas
        if (i == 0) return true;
        if (i > 0) {
            if (pixels_btm[i - 1][j] == 0) {
                return true;
            }
        }

        // check right -- kanan
        if (j == w) return true;
        if (j < w) {
            if (pixels_btm[i][j + 1] == 0) {
                return true;
            }
        }

        // check down -- bawah
        if (i == h) return true;
        if (i < h) {
            if (pixels_btm[i + 1][j] == 0) {
                return true;
            }
        }

        // no empty pixel around = not border pixel -- jika tidak ada pixel putih(kosong) maka border tidak akan terdeteksi
        return false;
    }

    //chancode dari pojok-kiri-atas
    public int[] borderNeighbors(int i, int j) {   //rumus chaincodes

        int index[] = new int[2];
        boolean flag = false;

        String bn = "";

        //cek jika di array pixels ada member yang bernilai 1 (hitam)
        //if(hitam == ada)

        // check around pixel for unvisited border pixels -- memeriksa piksel disekitar objek yang tidak terdeteksi bordernya

        // calculates chain codes distance -- mulai masuk ke rumus chaincodes

        // check east -- kode rantai 0
        if (borderPixel(i, j + 1) && !flag && !flag && visited[i][j + 1] == 0) {
            j = j + 1;
            System.out.print("0 ");
            bn = bn + "0 ";
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
        if (borderPixel(i + 1, j + 1) && !flag && visited[i + 1][j + 1] == 0) {
            i = i + 1;
            j = j + 1;
            System.out.print("7 ");
            bn = bn + "7 ";
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
        if (borderPixel(i + 1, j) && !flag && visited[i + 1][j] == 0) {
            i = i + 1;
            System.out.print("6 ");
            bn = bn + "6 ";
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
        if (borderPixel(i + 1, j - 1) && !flag && visited[i + 1][j - 1] == 0) {
            i = i + 1;
            j = j - 1;
            System.out.print("5 ");
            bn = bn + "5 ";
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
        if (borderPixel(i, j - 1) && !flag && visited[i][j - 1] == 0) {
            j = j - 1;
            System.out.print("4 ");
            bn = bn + "4 ";
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
        if (borderPixel(i - 1, j - 1) && !flag && visited[i - 1][j - 1] == 0) {
            i = i - 1;
            j = j - 1;
            System.out.print("3 ");
            bn = bn + "3 ";
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
        if (borderPixel(i - 1, j) && !flag && visited[i - 1][j] == 0) {
            i = i - 1;
            System.out.print("2 ");
            bn = bn + "2 ";
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
        if (borderPixel(i - 1, j + 1) && !flag && visited[i - 1][j + 1] == 0) {
            i = i - 1;
            j = j + 1;
            System.out.print("1 ");
            bn = bn + "1 ";
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

    //chancode dari pojok-kiri-atas
    public int[] borderNeighbors2(int i, int j) {   //rumus chaincodes

        int index[] = new int[2];
        boolean flag = false;

        //cek jika di array pixels ada member yang bernilai 1 (hitam)
        //if(hitam == ada)

        // check around pixel for unvisited border pixels -- memeriksa piksel disekitar objek yang tidak terdeteksi bordernya

        // calculates chain codes distance -- mulai masuk ke rumus chaincodes

        // check east -- kode rantai 0
        if (borderPixel2(i, j + 1) && !flag && !flag && visited_btm[i][j + 1] == 0) {
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
        if (borderPixel2(i + 1, j + 1) && !flag && visited_btm[i + 1][j + 1] == 0) {
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
        if (borderPixel2(i + 1, j) && !flag && visited_btm[i + 1][j] == 0) {
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
        if (borderPixel2(i + 1, j - 1) && !flag && visited_btm[i + 1][j - 1] == 0) {
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
        if (borderPixel2(i, j - 1) && !flag && visited_btm[i][j - 1] == 0) {
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
        if (borderPixel2(i - 1, j - 1) && !flag && visited_btm[i - 1][j - 1] == 0) {
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
        if (borderPixel2(i - 1, j) && !flag && visited_btm[i - 1][j] == 0) {
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
        if (borderPixel2(i - 1, j + 1) && !flag && visited_btm[i - 1][j + 1] == 0) {
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

    public void chainCodes2(int i, int j) {
        /*
        i e j = index of current pixel
        index[0], index[1] = next border pixel (if exists)
        */

        // coordinates of current pixel
        int[] index = new int[2];

        // check for border pixels around
        index = borderNeighbors2(i, j);

        // set pixel as visited
        visited_btm[i][j] = 1;

        // if next border pixel is visited, we're back to the first pixel
        if (visited_btm[index[0]][index[1]] == 0) {
            chainCodes2(index[0], index[1]);
        } else {
            System.out.println();
        }
    }

    public static String[] mainMethod(Bitmap bitmap, double jarakSebenarnya) {

        Chain c = new Chain(bitmap);

        // get key coordinates
        c.firstPixel();
        c.lastPixel();

        // generate chain codes
        // get coordinates of first border pixel after initial
        int[] index = new int[2];
        int[] index2 = new int[2];


        System.out.println();
        System.out.print("Chain Codes 1: ");
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
        String[] shape = new String[4];
        shape[0] = "SATUAN PIXEL 1 :\n" +
                "Shape width:  " + df2.format(c.width) + " pixels\n" +
                "Shape height: " + df2.format(c.height) + " pixels\n" +
                "Shape perimeter / keliling: " + df.format(c.perimeter) + " pixels\n" +
                "Shape area / luas: " + df.format(c.area) + " pixels\n";

        double jarakPadaPeta = c.width / 37.795;
        String skala = "1 : " + df.format(jarakSebenarnya / jarakPadaPeta);

        //skala
        // jarak pada peta : jarak sebenarnya
        // 11 cm : 4 cm
        // 1 cm : (4/11) cm
        // 1 cm : 0.2 cm
        // 1 : 0.2

        shape[1] = "SATUAN CENTIMETER 1 :\n" +
                "Shape width: " + df.format(c.width / 37.795) + " cm\n" +
                "Shape height: " + df.format(c.height / 37.795) + " cm\n" +
                "Shape perimeter / keliling: " + df.format(c.perimeter / 37.795) + " cm\n" +
                "Shape area / luas: " + df.format(c.area / Math.pow(37.795, 2)) + " cm\n" +
                "Skala: (" + skala + ")\n";

        System.out.println("SATUAN PIXEL 1 :");
        System.out.println("Shape width: " + df2.format(c.width) + " pixels");
        System.out.println("Shape height: " + df2.format(c.height) + " pixels");
        System.out.println("Shape perimeter / keliling: " + df.format(c.perimeter) + " pixels");
        System.out.println("Shape area / luas: " + df.format(c.area) + " pixels");
        System.out.println();

        //get all cm size
        System.out.println("SATUAN CENTIMETER 1 :");
        System.out.println("Shape width: " + df.format(c.width / 37.795) + " cm");
        System.out.println("Shape height: " + df.format(c.height / 37.795) + " cm");
        System.out.println("Shape perimeter / keliling: " + df.format(c.perimeter / 37.795) + " cm");
        System.out.println("Shape area / luas: " + df.format(c.area / Math.pow(37.795, 2)) + " cm");

        //get border shape
        c.border();
        System.out.println();
        System.out.println("Border pixels 1: " + c.points + " pixels");

        System.out.println("-------------------------------------------------");

        c.firstPixel2();
        c.lastPixel2();

        System.out.println();
        System.out.println();
        System.out.print("Chain Codes 2: ");
        index2 = c.borderNeighbors2(c.begin[0], c.begin[1]);

        c.chainCodes2(index2[0], index2[1]);
        c.setHeight2();
        c.setWidth2();

        shape[2] = "SATUAN PIXEL 2 :\n" +
                "Shape width:  " + df2.format(c.width) + " pixels\n" +
                "Shape height: " + df2.format(c.height) + " pixels\n" +
                "Shape perimeter / keliling: " + df.format(c.perimeter) + " pixels\n" +
                "Shape area / luas: " + df.format(c.area) + " pixels\n";

        shape[3] = "SATUAN CENTIMETER 2 :\n" +
                "Shape width: " + df.format(c.width / 37.795) + " cm\n" +
                "Shape height: " + df.format(c.height / 37.795) + " cm\n" +
                "Shape perimeter / keliling: " + df.format(c.perimeter / 37.795) + " cm\n" +
                "Shape area / luas: " + df.format(c.area / Math.pow(37.795, 2)) + " cm";

        //get all pixels size
        System.out.println();
        System.out.println("SATUAN PIXEL 2 :");
        System.out.println("Shape width: " + df2.format(c.width) + " pixels");
        System.out.println("Shape height: " + df2.format(c.height) + " pixels");
        System.out.println("Shape perimeter / keliling: " + df.format(c.perimeter) + " pixels");
        System.out.println("Shape area / luas: " + df.format(c.area) + " pixels");

        System.out.println();
        //get all cm size
        System.out.println();
        System.out.println("SATUAN CENTIMETER 2 :");
        System.out.println("Shape width: " + df.format(c.width / 37.795) + " cm");
        System.out.println("Shape height: " + df.format(c.height / 37.795) + " cm");
        System.out.println("Shape perimeter / keliling: " + df.format(c.perimeter / 37.795) + " cm");
        System.out.println("Shape area / luas: " + df.format(c.area / Math.pow(37.795, 2)) + " cm");


        c.border2();
        System.out.println();
        System.out.println("Border pixels 2: " + c.points + " pixels");

        return shape;
    }
}
