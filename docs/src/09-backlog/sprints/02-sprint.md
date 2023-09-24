# Sprint 2 [04/09/2023 - 10/09/2023]

Giornalemnte, viene calato il quantitativo effettivo

| Backlog Item                                                                                                    | Id  | Task                                                                                         | Volontario  | Stima (h) | Effettivo (h) | D1 | D2 | D3 | D4 | D5 | D6 | D7 |
|:----------------------------------------------------------------------------------------------------------------|:---:|:---------------------------------------------------------------------------------------------|-------------|:---------:|:-------------:|----|----|----|----|----|----|----|
| Integrare WartRemover                                                                                           |  0  | Eventuale                                                                                    | Andruccioli |     0     |       1       | 1  | 0  | 0  | 0  | 0  | 0  | 0  |
| Setup README                                                                                                    |  0  | Eventuale                                                                                    | Borriello   |     0     |       1       | 0  | 0  | 0  | 0  | 0  | 0  | 0  |
| Menù principale dell'applicazione & iniziare una partita                                                        | 4.1 | Integrare MVC con scalaJs                                                                    | Mazzoli     |     2     |       4       | 4  | 4  | 2  | 0  | 0  | 0  | 0  |
| Menù principale dell'applicazione & iniziare una partita                                                        | 4.2 | Creazione pagina menù principale                                                             | Borriello   |     2     |       2       | 0  | 0  | 0  | 0  | 0  | 0  | 0  |
| Menù principale dell'applicazione & iniziare una partita                                                        | 4.3 | Creazione pagina di inizio partita                                                           | Borriello   |     1     |       1       | 1  | 1  | 0  | 0  | 0  | 0  | 0  |
| Menù principale dell'applicazione & iniziare una partita                                                        | 4.4 | Creazione pagina di gioco                                                                    | Borriello   |     1     |       1       | 1  | 1  | 1  | 1  | 1  | 0  | 0  |
| Menù principale dell'applicazione & iniziare una partita                                                        | 4.5 | Doc MVC                                                                                      | Mazzoli     |     2     |       2       | 2  | 2  | 2  | 2  | 2  | 0  | 0  |
| Come giocatore, voglio vedere la mappa e i numeri che indicano quali lanci produrranno risorse per ogni tessera | 5.1 | Model Mappa                                                                                  | Team        |     8     |      20       | 16 | 12 | 8  | 4  | 0  | 0  | 0  |
| Come giocatore, voglio vedere la mappa e i numeri che indicano quali lanci produrranno risorse per ogni tessera | 5.2 | Mostrare graficamente la mappa                                                               | Andruccioli |     4     |       6       | 6  | 6  | 6  | 6  | 4  | 2  | 0  |
| Come giocatore, voglio gestire il posizionamento iniziale                                                       | 6.1 | Gestione input dalla view                                                                    | Team        |     4     |       2       | 4  | 4  | 4  | 4  | 4  | 4  | 2  |
| Come giocatore, voglio gestire il posizionamento iniziale                                                       | 6.2 | Update del model a fronte dell'input dell'utente                                             | -           |     2     |       -       | -  | -  | -  | -  | -  | -  | -  |
| Come giocatore, voglio gestire il posizionamento iniziale                                                       | 6.3 | Update della view in base al cambio del model                                                | -           |     2     |       -       | -  | -  | -  | -  | -  | -  | -  |
| Come giocatore, voglio ricevere le materie prime iniziali                                                       | 7.1 | Aggiornamento delle risorse dei giocatori in base ai posizionamenti iniziali e print a video | -           |     4     |       -       | -  | -  | -  | -  | -  | -  | -  |
| **TOT**                                                                                                         |     |                                                                                              | -           |    32     |      40       | 35 | 30 | 23 | 17 | 11 | 6  | 2  |

## Sprint goal

Gli obiettivi di questa Sprint sono stati:

- Setup delle view in diverse schemate.
- Definizione del model della Mappa.
- Mostrare a video la mappa.

## Deadline

10/09/2023

## Sprint review

I task finali, come da backlog, previsti dallo sprint, non sono stati completati, in favore di un'analisi più
approfondita del model della mappa. Questa fase è stata identificata come parte fondamentale e delicata per il corretto
ed agevole svolgimento, a seguire, del progetto, perciò è stato deciso di dedicargli più tempo di quanto stimato a
priori.

## Sprint retrospective

Nel secondo sprint si è puntato ad ottenere un prodotto tangibile, seguendo la filosofia Agile.
Quindi, si è optato per puntare subito per lo sviluppo delle prime View in modo da poter man mano intergrare gli
sviluppi e renderli visualizzabili da un eventuale consumer.

### Task 5.1

Il task 5.1 (model mappa), è stato il task più complesso di questo sprint, in quanto ha richiesto un'analisi
approfondita del dominio e non, al fine di modellare al meglio la mappa.

Si è adottato un approccio dove, ogni membro del team, ha analizzato il dominio e ha proposto una soluzione, che è stata
poi discussa e confrontata con gli altri componenti.

Alla fine, si è optato per la soluzione proposta da Manuel Andruccioli, in quanto più completa, modulare, facilmente
estendibile ma soprattuto, più fedele al dominio.
