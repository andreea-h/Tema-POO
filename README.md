# Tema-POO

In cadrul cerintei 1, Implementarea si testarea aplicatiei (fara interfata grafica), am definit in cadrul clasei Test metode apelate in functie de continutul fisierului "events.txt", in cadrul fiecarei metode apelandu-se metode corespunzatoare din interiorul ierarhiei de clase. In metoda 'main' a clasei Test, am folosit 2 apeluri, unul in care se face testarea fara intrefata grafica, si cel de-al doilea pentru afisarea ferestrei principale a interfetei (prin ascultarea componentelor din aceasta fereastra se activeaza si alte ferestre din interfata). Am comentat in 'main' apelul pentru testarea fara interfata grafica (adica partea in care se evalueaza 'events.txt' si se scrie in 'output.txt').

	Fereastra de start:
![alt-text](https://github.com/andreea-h/Tema-POO/blob/master/pagina_start.png)

	Mesaj de atentionare catre utilizator:
![alt-text](https://github.com/andreea-h/Tema-POO/blob/master/gestionare_input_eronat.png)

In fereastra principala a interfetei grafice am adaugat 2 butoane, 'Load users' si 'Load campaigns' care adauga userii si campaniile din fisierele 'users.txt' si 'campaigns.txt'. Butonul de login devine accesibil dupa ce au fost adaugati userii. La logarea unui admin se afiseaza AdminWindow, cu optiunile specificate prin butoane: "administrare campanii", "generare mulipla vouchere"(la apasarea acestui buton se deschide o fereastra in cadrul careia se introduce intr-un JtextField id-ul campaniei pentru care se doreste distribuirea si un tabel in care se afiseaza voucherele generate) si "activeaza optiunea de actualizare automata a statusului campaniilor".
	
	Pagina de start a utilizatorului de tip admin:
![alt-text](https://github.com/andreea-h/Tema-POO/blob/master/pagina_start_admin.png)

In cadrul ferestrei de administrare de campanii (implementare in CampaignWindow) am folosit componente de tip JButton pentru activarea ferestrelor de 'Gestionare de vouchere a campaniei' sau pentru fereastra de vizualizare a observatorilor campaniei selectate din tabel (daca nu este selectata o campanie din tabel se afiseaza o fereastra de eroare (implementare in 'ErrorWindow') cu mesajul aferent. Afisarea informatiilor despre o campania selectata din tabel se face in cadrul unui JPanel in care am adaugat mai multe componente de tip JLabel (acesta devine vizibil dupa apasarea butonului cu mesajul 'Vizualizare informatii complete despre campanie). Inchiderea unei campanii se face la apasarea butonului cu mesajul aferent; daca nu este selectata nicio campanie din tabel sau daca nu este valida operatia de inchidere a campaniei selectate (campania este 'EXPIRED' sau 'CANCELLED' se afiseaza un mesaj de eroare corespunzator folosind o instanta a clasei ErroWindow - mesajul este trimis ca argument al constructorului clasei).

	Pagina de vizualizare a campaniilor:
![alt-text](https://github.com/andreea-h/Tema-POO/blob/master/pagina_campanii.png)	
	Pagina de start a unui user obisnuit (de tip guest):
![alt-text](https://github.com/andreea-h/Tema-POO/blob/master/pagina_start_user.png)
	
In ceea ce priveste bonusul pentru intrefata grafica, am implementat pagina in care un utilizator isi poate vedea notificatile si pagina in care se afiseaza observatorii unei anumite campanii si voucherele utilizate in cadrul acelei campanii. In cadrul paginii de notificari a userului am adaugat o componenta de tip JTextArea in care am introdus, de fiecare data pe o linie noua, pentru fiecare notificare din lista de notificari a userului, un string continand informatii despre modificarea adusa unei anume campanii (data la care s-a produs modificarea, tipul modificarii - de EDIT sau CANCEL) si codurile voucherelor detinute de user in cadrul acelei campanii.
	
In cadrul paginii de vizualizare a observatorilor unei campanii am introdus un tabel in care am afisat userii, introducand si o coloana pentru numarul de vouchere utilizate de acel user in cadrul campaniei curente. Numarul total de vouchere utilizate este afisat in cadrul unei JLabel.
	
Am implementat, de asemenea, optiunea de distribuire multipla de vouchere si functionalitatea de verificare si editare periodica a statusului campaniilor, in functie de data curenta si cea de start/finalizare a acestora - cea de-a doua operatie devine activa dupa apasarea butonului cu mesajul analog din pagina principala a userului admin. Totodata, in implementare am respectat 'Strategy pattern' pentru optiunea de gestionare de vouchere conform unei anumite strategii (in interfata grafica am adaugat un buton cu mesajul analog in fereasta de administrare de vouchere a unei campanii - acest buton devine activ doar dupa ce s-a generat minim un voucher in cadrul acelei campanii).
