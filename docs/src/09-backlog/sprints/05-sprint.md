# Sprint 5 [08/10/2023 - 14/10/2023]

Giornalemnte, viene calato il quantitativo effettivo.

| Backlog Item                                                                                                                                             |  Id  | Task                                                                                 | Volontario  | Stima (h) | Effettivo (h) | D1 | D2 | D3 | D4 | D5 | D6 |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------|:----:|:-------------------------------------------------------------------------------------|-------------|:---------:|:-------------:|----|----|----|----|----|----|
| Eventuale                                                                                                                                                |  0   | Refactor view components                                                             | Andruccioli |     -     |       4       | 2  | 2  | 2  | 2  | 0  | 0  |
| Eventuale                                                                                                                                                |  0   | Nuovo DSL                                                                            | Mazzoli     |     -     |       8       | 2  | 0  | 0  | 0  | 0  | 0  |
| Eventuale                                                                                                                                                |  0   | Aggiungere victory points dev cards come criterio di calcolo dello score             | Borriello   |     -     |       1       | 0  | 0  | 0  | 0  | 0  | 0  |
| Eventuale                                                                                                                                                |  0   | Refactor CardOps                                                                     | Borriello   |     -     |       2       | 2  | 2  | 2  | 2  | 2  | 0  |
| Come giocatore, voglio poter scambiare risorse con altri giocatori                                                                                       | 11.2 | Implementazione Trade con giocatore lato View                                        | Borriello   |     4     |       4       | 4  | 4  | 2  | 2  | 0  | 0  |
| Come giocatore, voglio poter costruire strade, colonie, città ed acquistare carte sviluppo usando le risorse appropriate e con i vincoli del regolamento | 12.5 | Implementazione bottone di acquisto carte sviluppo                                   | Mazzoli     |     1     |       1       | 1  | 0  | 0  | 0  | 0  | 0  |
| Come giocatore, voglio poter costruire strade, colonie, città ed acquistare carte sviluppo usando le risorse appropriate e con i vincoli del regolamento | 12.6 | Controllare fattibilità costruzioni come da regolamento                              | Andruccioli |     4     |       4       | 4  | 2  | 2  | 0  | 0  | 0  |
| Come giocatore, voglio poter scambiare con la banca, mediante i giusti rapporti                                                                          | 13.1 | Implementazione Trade con Banca lato Game                                            | Borriello   |     1     |       3       | 3  | 2  | 0  | 0  | 0  | 0  |
| Come giocatore, voglio poter scambiare con la banca, mediante i giusti rapporti                                                                          | 13.2 | Implementazione Trade con Banca lato View                                            | Borriello   |     2     |       2       | 2  | 2  | 0  | 0  | 0  | 0  |
| Come giocatore, voglio poter giocare carte sviluppo                                                                                                      | 14.1 | Gestione play delle carte sviluppo                                                   | Mazzoli     |     8     |       4       | 4  | 4  | 4  | 0  | 0  | 0  |
| Come giocatore, voglio poter vedere che giocatore detiene i certificati                                                                                  | 16.2 | Aggiungere alla View chi detiene gli award                                           | Borriello   |     1     |       1       | 0  | 0  | 0  | 0  | 0  | 0  |
| Gestione corretta di "7 ai dadi"                                                                                                                         | 17.2 | Implementazione azione di furto carta dopo aver piazzato il robber lato Game         | Mazzoli     |     1     |       1       | 1  | 1  | 0  | 0  | 0  | 0  |
| Gestione corretta di "7 ai dadi"                                                                                                                         | 17.3 | Implementare furto di carta lato View                                                | Mazzoli     |     1     |       1       | 1  | 1  | 0  | 0  | 0  | 0  |
| Come giocatore, voglio essere informato quando il gioco è finito e vedere i risultati finali.                                                            | 19.1 | Mostrare lato View il punteggio del giocatore attuale e/o di tutti i giocatori       | Mazzoli     |     1     |       1       | 1  | 1  | 1  | 0  | 0  | 0  |
| Come giocatore, voglio essere informato quando il gioco è finito e vedere i risultati finali.                                                            | 19.2 | Banner di fine gioco                                                                 | Mazzoli     |     1     |       1       | 1  | 1  | 1  | 0  | 0  | 0  |
| Come giocatore, voglio poter giocare con mappe diverse                                                                                                   | 20.1 | Generazione random della mappa                                                       | Andruccioli |     1     |       2       | 2  | 2  | 2  | 2  | 0  | 0  |
| Come giocatore, voglio poter giocare con mappe diverse                                                                                                   | 20.2 | Aggiungere la preview della mappa, con la possibilità di sceglierla in fase di setup | Andruccioli |     2     |       4       | 3  | 3  | 3  | 3  | 2  | 0  |
| **TOT**                                                                                                                                                  |      |                                                                                      |             |    28     |      44       | 31 | 25 | 17 | 9  | 2  | 0  |

## Sprint goal

Gli obiettivi di questa Sprint sono stati:

- Implementazione dei Trade con altri player e con la banca
- Implementazione controlli durante la costruzione di strutture
- Gestione carte sviluppo
- Gestione 7 ai dadi
- Gestione fine gioco
- Gestione cambio mappa
- Code Cleanup & Refactor

## Deadline

13/10/2023

## Sprint review

Gli obiettivi prefissati nello sprint sono stati raggiunti con successo il giorno della deadline.

## Sprint retrospective

Lo sprint, come previsto, è stato meno impegnativo dei precedenti poichè si era raggiunto un stato del core stabile e
facilmente estendibile.
Questo ha reso l'implementazione delle ultime feature molto semplice.
Inoltre, essendo l'ultimo sprint, parte di esso è stato dedicato alla pulizia del codice e alla documentazione mancante.
