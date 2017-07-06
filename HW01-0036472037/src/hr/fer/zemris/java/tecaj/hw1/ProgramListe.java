package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program koji demonstrira listu.
 * Dodaje elemente u listu, ispisuje elemente liste, 
 * broji koliko ima elemenata u listi, te sortira listu. 
 * 
 * @author Ivica B.
 * @version 1.0
 *
 */
class ProgramListe {
	
	/**
	 * Definira strukturu jednog čvora liste.
	 * 
	 * @author Ivica B.
	 * @version 1.0
	 *
	 */
	static class CvorListe {
		/**
		 * cvor koji slijedi , ili null ako je trenutni zadnji
		 */
		CvorListe sljedeci;
		/**
		 * podatak u trenutnom cvoru
		 */
		String podatak;
	}

	/**
	 * Metoda koja pokreće program ProgramListe.
	 * @param args argumenti programa- ovdje se ne koriste
	 */
	public static void main(String[] args) {
		CvorListe cvor = null;
		cvor = ubaci(cvor, "Jasna");
		cvor = ubaci(cvor, "Ana");
		cvor = ubaci(cvor, "Ivana");
		System.out.println("Ispisujem listu uz originalni poredak:");
		ispisiListu(cvor);
		cvor = sortirajListu(cvor);
		System.out.println("Ispisujem listu nakon sortiranja:");
		ispisiListu(cvor);
		int vel = velicinaListe(cvor);
		System.out.println("Lista sadrzi elemenata: " + vel);
	}

	/**
	 * Vraća broj elemenata liste.
	 * Kao argument se mora predati prvi čvor liste.
	 * @param cvor prvi čvor liste
	 * @return broj elemenata liste koja počinje sa čvorom cvor
	 */
	private static int velicinaListe(CvorListe cvor) {
		int vel = 0;
		while(cvor != null) {
			vel++;
			cvor = cvor.sljedeci;
		}
		return vel;
	}

	/**
	 * Dodaje element u listu.
	 * Element se dodaje na početak liste.
	 * @param prvi	prvi čvor liste
	 * @param podatak element koji se dodaje u listu
	 * @return novostvoreni čvor, koj je od sada i prvi čvor liste
	 */
	private static CvorListe ubaci(CvorListe prvi, String podatak) {
		CvorListe novi = new CvorListe();
		novi.podatak = podatak;
		novi.sljedeci = prvi;
		return novi;
	}

	/**
	 * Ispisuje sve elemente liste krenuvši od prvog.
	 * @param cvor prvi čvor liste
	 */
	private static void ispisiListu(CvorListe cvor) {
		while(cvor != null) {
			System.out.println(cvor.podatak);
			cvor = cvor.sljedeci;
		}
	}

	/**
	 * Uzlazno sortira elemente liste.
	 * @param cvor prvi čvor liste
	 * @return prvi čvor sortire liste
	 */
	private static CvorListe sortirajListu(CvorListe cvor) {
		if(velicinaListe(cvor) < 2) {
			return cvor;
		}
		boolean sort = true;
		CvorListe start = cvor;
		while(sort) {
			sort = false;
			cvor = start;
			while(cvor.sljedeci != null) {
				if(cvor.podatak.compareTo(cvor.sljedeci.podatak) > 0) {
					String temp = cvor.podatak;
					cvor.podatak = cvor.sljedeci.podatak;
					cvor.sljedeci.podatak = temp;
					sort = true;
				}
				cvor = cvor.sljedeci;
			}
		}
		return start;
	}
}