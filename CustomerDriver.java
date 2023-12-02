import java.util.*;

/**
 * Kelas {@code CustomerDriver} merupakan pengemudi (driver) untuk pengguna dengan peran pelanggan.
 * Kelas ini menyediakan berbagai fungsi untuk berinteraksi dengan sistem, seperti melihat list barang,
 * melihat keranjang belanja, melihat invoice, dan melakukan checkout.
 *
 * <p>
 * Author: Aulia Vika Rahman
 * </p>
 */
public class CustomerDriver extends Driver {

  /**
     * Akun pelanggan yang sedang aktif.
     */
  public Customer akun;

  /**
     * Daftar barang yang tersedia.
     */
  public ListBarang listBarang;

    /**
     * Transaksi yang sedang berlangsung.
     */
  public Transaksi transaksi;

  /**
     * Daftar transaksi yang telah dilakukan oleh pelanggan.
     */
  public ArrayList<Transaksi> listTransaksi;

  /**
     * Membuat objek {@code CustomerDriver} dengan akun pelanggan, daftar barang, dan objek transaksi yang diberikan.
     *
     * @param akun      Akun pelanggan yang sedang aktif.
     * @param listBarang Daftar barang yang tersedia.
     * @param transaksi Transaksi yang sedang berlangsung.
     */
  public CustomerDriver(
    Customer akun,
    ListBarang listBarang,
    Transaksi transaksi
  ) {
    this.akun = akun;
    this.listBarang = listBarang;
    this.transaksi = transaksi;
  }

   /**
     * Menampilkan menu  untuk pelanggan dan memproses input mereka.
     */
  public void menu() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.println("\n=========================");
      System.out.println("|      MENU CUSTOMER    |");
      System.out.println("=========================");
      System.out.println("|  1. Lihat List Barang |");
      System.out.println("|  2. Lihat Keranjang   |");
      System.out.println("|  3. Lihat Invoice     |");
      System.out.println("|  0. Logout            |");
      System.out.println("=========================");

      try{
      System.out.print("Pilihan Anda: ");
      int pilih = scanner.nextInt();

      switch (pilih) {
        case 1:
          lihatListBarangDanTambahKeKeranjang();
          break;
        case 2:
          lihatKeranjang();
          if(!akun.keranjang.barang.isEmpty()){
          System.out.print("Apakah Anda ingin checkout sekarang? (ya/tidak): ");
          String checkoutSekarang = scanner.next().toLowerCase();
          if (checkoutSekarang.equals("ya")) {
            checkout();
          }
          }
          break;
        case 3:
          lihatDaftarInvoice();
          break;
        case 0:
          System.out.println("Logout berhasil.");
          return;
        default:
          System.out.println("Pilihan tidak valid.");
      }
    }catch(InputMismatchException e){
         System.out.println("Masukkan harus berupa angka.");
            scanner.nextLine();  // Membersihkan buffer setelah input yang salah
            continue;
    }
    }
  }
  
  /**
     * Menampilkan list barang yang tersedia dan memberikan opsi untuk menambahkan barang ke keranjang.
     */
  private void lihatListBarangDanTambahKeKeranjang() {
    System.out.println("\n================ LIST BARANG ================");
    System.out.println("---------------------------------------------");
    System.out.printf("|    %-12s |     %-9s |   %-6s|%n", "Nama", "Harga", "Stok");
    System.out.println("---------------------------------------------");

    for (Barang barang : listBarang.barang) {
        System.out.printf("|    %-12s |  Rp.%-8d  |    %-5d|%n", barang.nama, barang.harga, barang.stok);
        System.out.println("---------------------------------------------");
    }
    System.out.print(
      "Apakah Anda ingin menambahkan barang ke keranjang? (ya/tidak): "
    );
    Scanner scanner = new Scanner(System.in);
    String tambahkanKeKeranjang = scanner.next().toLowerCase();

    if (tambahkanKeKeranjang.equals("ya")) {
      System.out.print(
        "Masukkan nama barang yang akan ditambahkan ke keranjang : "
      );
      String namaBarang = scanner.next();

      for (Barang barangKeranjang : akun.keranjang.barang) {
        if (barangKeranjang.nama.equals(namaBarang)) {
          System.out.print("Masukkan jumlah " + namaBarang + " yang ingin anda tambahkan : ");
          int jumlahTambahKeranjang = scanner.nextInt();

          for (Barang barang : listBarang.barang) {
            if (
              barang.nama.equals(namaBarang) &&
              jumlahTambahKeranjang > barang.stok
            ) {
              System.out.println("Stok tidak mencukupi.");
              return;
            }
          }
         
          barangKeranjang.stok += jumlahTambahKeranjang;
 
          for (Barang barang : listBarang.barang) {
            if (barang.nama.equals(namaBarang)) {
              barang.stok -= jumlahTambahKeranjang;
              break;
            }
          }

          System.out.println("Jumlah barang diperbarui di keranjang.");
          return;
        }
      }

      // Barang belum ada di keranjang, tambahkan sebagai barang baru
      for (Barang barang : listBarang.barang) {
        if (barang.nama.equals(namaBarang)) {
          System.out.print(
            "Masukkan jumlah " + namaBarang + " yang ingin anda beli :"
          );

          int jumlahBarangKeranjangTambah = scanner.nextInt();

          if (jumlahBarangKeranjangTambah > barang.stok) {
            System.out.println("Stok tidak mencukupi.");
            return;
          }

          Barang barangKeranjang = new Barang(
            barang.nama,
            barang.harga,
            jumlahBarangKeranjangTambah
          );

          akun.keranjang.barang.add(barangKeranjang);

          barang.stok -= jumlahBarangKeranjangTambah;

          System.out.println("Barang ditambahkan ke keranjang.");
          return;
        }
      }
      System.out.println("Barang tidak ditemukan.");
    }
  }

  /**
     * Menampilkan isi keranjang belanja pelanggan beserta total harga.
     */
  public void lihatKeranjang() {
    if (akun.keranjang.barang.isEmpty()) {
        System.out.println("\nKeranjang anda kosong");
    } else {
        System.out.println("================= KERANJANG ANDA ==============");
        System.out.println("--------------------------------------------------");
        System.out.println("| Nama           | Harga         |     Jumlah    |");
        System.out.println("--------------------------------------------------");
        int totalHargaKeranjang = 0;

        for (Barang barang : akun.keranjang.barang) {
            System.out.printf("| %-14s | Rp.%-10d |        %-7d|%n", barang.nama, barang.harga, barang.stok);

            // Hitung total harga untuk barang di keranjang, dengan mempertimbangkan stok
            int totalHargaBarang = barang.harga * barang.stok;
            System.out.println("|                |               |----------------");
            System.out.printf("|                |               |  Rp %-8d |%n", totalHargaBarang);
            System.out.println("--------------------------------------------------");
            

            totalHargaKeranjang += totalHargaBarang;
        }

        // Tampilkan total harga untuk seluruh keranjang
        System.out.println("| Total Harga                    |  Rp " + totalHargaKeranjang+" |" );
        System.out.println("--------------------------------------------------");
    }
}

 /**
     * Melakukan proses checkout untuk pembelian barang dalam keranjang.
     * Pelanggan dapat memilih metode pembayaran seperti QRIS, COD, atau Bank Transfer.
     */
  public void checkout() {
    if (akun.keranjang.barang.isEmpty()) {
      System.out.println(
        "\nKeranjang kosong. Tidak dapat melakukan checkout.\n"
      );
      return;
    }

    System.out.println("\nPilih metode pembayaran:");
    System.out.println("1. QRIS");
    System.out.println("2. COD");
    System.out.println("3. Bank Transfer");

    Scanner scanner = new Scanner(System.in);
    System.out.print("\nPilihan anda: ");
    int pilih = scanner.nextInt();

    Transaksi currentTransaksi = new Transaksi(akun,new ArrayList<>(akun.keranjang.barang)
    );
    Invoice invoice = null;

    switch (pilih) {
      case 1:
        invoice = currentTransaksi.generateInvoice(new Qris(), new Date());
        ((Qris) invoice.pembayaran).tampilkanInstruksiPembayaran();
        break;
      case 2:
        invoice = currentTransaksi.generateInvoice(new COD(), new Date());
        ((COD) invoice.pembayaran).tampilkanInstruksiPembayaran();
        break;
      case 3:
        Bank bankTransfer = new Bank("735032050");
        invoice = currentTransaksi.generateInvoice(bankTransfer, new Date());
        bankTransfer.tampilkanInstruksiPembayaran();
        break;
      default:
        System.out.println("Pilihan tidak valid.");
        return;
    }

    AdminDriver adminDriver = new AdminDriver(listBarang, listTransaksi);
    akun.keranjang.transaksi = currentTransaksi;
    adminDriver.addListTransaksi(currentTransaksi);

    akun.invoiceSelesai.add(invoice);
    akun.keranjang.barang.clear();

    System.out.println("Checkout berhasil. Terima kasih!");
  }

  /**
     * Menampilkan daftar invoice.
     */
  private void lihatDaftarInvoice() {
    System.out.println("\nDaftar Invoice Anda:");
    if (akun.invoiceSelesai.isEmpty()) {
      System.out.println("Belum ada invoice yang selesai.");
      return;
    }

    for (Invoice invoice : akun.invoiceSelesai) {
      if (invoice.transaksi.acceptedByAdmin) {
        invoice.printInvoice();
      } else {
        System.out.println("Transaksi belum diterima oleh admin.");
      }
    }
  }
}