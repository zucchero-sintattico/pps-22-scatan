# Sprint 4 [25/09/2023 - 08/10/2023]

Giornalemnte, viene calato il quantitativo effettivo.

| Backlog Item                                                                                                                                             |  Id  | Task                                                                                                | Volontario  | Stima (h) | Effettivo (h) | D1 | D2 | D3 | D4 | D5 | D6 | D7 | D8 | D9 | D10 | D11 | D12 | D13 | D14 |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------|:----:|:----------------------------------------------------------------------------------------------------|-------------|:---------:|:-------------:|----|----|----|----|----|----|----|----|----|-----|-----|-----|-----|-----|
| Eventuale                                                                                                                                                |  0   | Refactor State scomposizione in più Ops files                                                       | Borriello   |     -     |       5       | 5  | 5  | 5  | 5  | 5  | 5  | 5  | 2  | 0  | 0   | 0   | 0   | 0   | 0   |
| Eventuale                                                                                                                                                |  0   | Creazione di un Model reattivo per la View                                                          | Mazzoli     |     -     |       4       | 4  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0   | 0   | 0   | 0   | 0   |
| Eventuale                                                                                                                                                |  0   | Upgrade struttura del Game e del DSL per supportare giochi basati su più fasi e step e cambio turno | Mazzoli     |     -     |       8       | 8  | 8  | 8  | 8  | 8  | 8  | 2  | 0  | 0  | 0   | 0   | 0   | 0   | 0   |
| Eventuale                                                                                                                                                |  0   | Creazione dello ScatanGame come layer semplificativo                                                |             |     -     |       2       | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 0   | 0   | 0   | 0   | 0   |
| Come giocatore, voglio gestire il posizionamento iniziale                                                                                                | 6.3  | Aggiunta Spot a Building                                                                            | Andruccioli |     2     |       4       | 4  | 4  | 4  | 4  | 2  | 0  | 0  | 0  | 0  | 0   | 0   | 0   | 0   | 0   |
| Come giocatore, voglio gestire il posizionamento iniziale                                                                                                | 6.4  | Creazione pagina per setup iniziale del gioco (assegnazione Player - Buildings)                     | Andruccioli |     1     |       2       | 2  | 2  | 2  | 2  | 2  | 0  | 0  | 0  | 0  | 0   | 0   | 0   | 0   | 0   |
| Come giocatore, voglio gestire il posizionamento iniziale                                                                                                | 6.5  | Start del gioco creando lo stato a partire dai buildings dei players                                | Andruccioli |     1     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 0  | 0  | 0  | 0   | 0   | 0   | 0   | 0   |
| Come giocatore, voglio ricevere le materie prime iniziali                                                                                                | 7.2  | Calcolo risorse iniziali a partire dai building dei giocatori                                       | Borriello   |     1     |       5       | 5  | 5  | 5  | 5  | 5  | 5  | 5  | 5  | 2  | 0   | 0   | 0   | 0   | 0   |
| Come giocatore, voglio ricevere le materie prime iniziali                                                                                                | 7.3  | Invocare assegnazione risorse iniziali al cambio di fase                                            | Mazzoli     |     1     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1   | 1   | 1   | 1   | 0   |
| Come giocatore, voglio vedere le carte che possiedo                                                                                                      | 8.3  | Visualizzazione carte del giocatore di turno in modo reattivo                                       | Andruccioli |     2     |       4       | 4  | 4  | 4  | 4  | 4  | 2  | 2  | 2  | 2  | 2   | 2   | 2   | 0   | 0   |
| Come giocatore, voglio lanciare i dati e raccogliere le risorse in base al risultato                                                                     | 10.1 | Implementazione azione di tiro dei dadi e assegnazione risorse                                      | Borriello   |     4     |       2       | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2   | 2   | 2   | 0   | 0   |
| Come giocatore, voglio lanciare i dati e raccogliere le risorse in base al risultato                                                                     | 10.2 | Implementazione azione lato View e mostrare risultato                                               | Andruccioli |     4     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1   | 1   | 1   | 1   | 0   |
| Come giocatore, voglio poter scambiare risorse con altri giocatori                                                                                       | 11.1 | Implementazione azione Trade with Player                                                            | Borriello   |     2     |       2       | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2   | 2   | 0   | 0   | 0   |
| Come giocatore, voglio poter costruire strade, colonie, città ed acquistare carte sviluppo usando le risorse appropriate e con i vincoli del regolamento | 12.1 | Implementare l'action di build                                                                      | Borriello   |     4     |       1       | 1  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0   | 0   | 0   | 0   | 0   |
| Come giocatore, voglio poter costruire strade, colonie, città ed acquistare carte sviluppo usando le risorse appropriate e con i vincoli del regolamento | 12.2 | Modellare il deck di carte sviluppo                                                                 | Mazzoli     |     4     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1   | 1   | 1   | 0   | 0   |
| Come giocatore, voglio poter costruire strade, colonie, città ed acquistare carte sviluppo usando le risorse appropriate e con i vincoli del regolamento | 12.3 | Implementare la funzione di buy development card                                                    | Mazzoli     |     4     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1   | 1   | 1   | 0   | 0   |
| Come giocatore, voglio poter costruire strade, colonie, città ed acquistare carte sviluppo usando le risorse appropriate e con i vincoli del regolamento | 12.4 | Resa grafica development card                                                                       | Andruccioli |     4     |       2       | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2  | 2   | 2   | 2   | 0   | 0   |
| Come giocatore, voglio vedere di chi è il turno e informazioni relative                                                                                  | 15.1 | Visualizzazione player di turno nella schermata di gioco (da rendere reattivo)                      | Andruccioli |     2     |       2       | 2  | 2  | 2  | 2  | 2  | 2  | 0  | 0  | 0  | 0   | 0   | 0   | 0   | 0   |
| Come giocatore, voglio vedere di chi è il turno e informazioni relative                                                                                  | 15.2 | Visualizzazione fase del turno                                                                      | Mazzoli     |     1     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1   | 1   | 1   | 1   | 0   |
| Come giocatore, voglio vedere di chi è il turno e informazioni relative                                                                                  | 15.3 | Visualizzazione azioni disponibili nella fase corrente                                              | Andruccioli |     1     |       2       | 2  | 2  | 2  | 2  | 2  | 0  | 0  | 0  | 0  | 0   | 0   | 0   | 0   | 0   |
| Gestione corretta di "7 ai dadi"                                                                                                                         | 17.1 | Implementazione azione place robber                                                                 | Borriello   |     1     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1   | 1   | 1   | 1   | 0   |
| Il brigante blocca la produzione della casella sottostante                                                                                               | 18.1 | Modellazione del brigante                                                                           | Borriello   |     3     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1   | 0   | 0   | 0   | 0   |
| Il brigante blocca la produzione della casella sottostante                                                                                               | 18.2 | Modifica dell'effetto di roll nello ScatanGame per nascondere la differenza tra Roll e RollSeven    | Mazzoli     |     3     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1   | 1   | 0   | 0   | 0   |
| Il brigante blocca la produzione della casella sottostante                                                                                               | 18.3 | Resa grafica del brigante                                                                           | Andruccioli |     2     |       1       | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1  | 1   | 1   | 1   | 0   | 0   |
| **TOT**                                                                                                                                                  |      |                                                                                                     |             |    47     |      55       | 55 | 50 | 50 | 50 | 48 | 40 | 31 | 26 | 21 | 17  | 16  | 13  | 4   | 0   |

## Sprint goal

Gli obiettivi di questa Sprint sono stati:

- Gestione interfacce reattiva
- Refactor gestione turni e fasi di gioco.
- Refactor dello State e delle relative operazioni
- Completamento gestione fase di posizionamento iniziale e relativa acquisizione di risorse
- Modellazione del tiro dei dadi e relativa acquisizione di risorse
- Modellazione carte sviluppo con relativo acquisto
- Modellazione brigante con relativo spostamento

## Deadline

08/10/2023

## Sprint review

Gli obiettivi prefisatti nello sprint sono stati raggiunti, espandendo notevolmente il valore del prodotto rilasciato.
Inoltre, sono stati recuperati i task lasciati indietro negli sprint precedenti.
Grazie allo stato attuale del core, è stato semplice realizzare anche alcuni task inizialmente non previsti, con
l'obiettivo di portarsi avanti per gli sprint futuri.

Infine, il team suppone che gran parte della modellazione sia stata realizzata, prevedendo che il lavoro a venire
sarà prettamente incentrato su implementazioni di funzionalità e/o raffinamenti.

## Sprint retrospective

Si è scelto di effettuare uno sprint da 2 settimane poichè due membri del team sono stati impegnati per quasi la
totalità della prima settimana per impegni personali. Così facendo, abbiamo avuto modo di recuperare il ritardo
accumulato e di fare la stessa quantità di lavoro prevista per gli altri sprint.

Lo sprint è stato uno dei più profiqui finora, poichè ha portato ad un core del gioco funzionante, stabile e facilmente
estendibile, che ha permesso di sviluppare le feature successive in modo più rapido rispetto alle tempistiche previste
all'inizio.