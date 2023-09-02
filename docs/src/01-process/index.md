# Processo di sviluppo adottato

Il processo di sviluppo adottato è SCRUM-inspired, che è stato scelto basandosi alle linee guida del corso, in modo da gestire in maniera agile il progetto.

La qualità del progetto è favorita dall'esperienza del team di sviluppo, guidate dalle buoni prassi di _Ingegneria del Software_ e supportata da sia da strumenti automatici, ma anche per interazione tra i membri.

## Ruoli

Nella fase iniziale del processo, sono stati definiti i seguenti ruoli:

- L'_esperto di dominio_: Alessandro Mazzoli, responsabile della qualità e usabilità del prodotto.
- Il _product owner_: Manuel Andruccioli, responsabile di supervisionare l'andamento del progetto e del coordinamento del team di sviluppo.

## Meeting

Le tipologie di meeting utilizzate sono le seguenti:

- **Initial planning**: meeting di inizio progetto.
  Esso è stato svolto all'inizio del progetto, e ha coinvolto tutti i membri del team.
  Lo scopo di questo meeting è quello di definire il _Product Backlog_, ovvero l'insieme di tutti i requisiti del progetto, e di definire gli obiettivi dello sprint iniziale.

- **Sprint planning**: meeting di pianificazione della durata di circa 1 ora.
  Esso viene svolto all'inizio di ogni sprint, ovvero ogni Lunedì, e coinvolge tutti i membri del team.
  Lo scopo di questo meeting è quello di selezionare quali item dal Product Backlog verranno sviluppati durante lo sprint, e di definire le attività da svolgere per raggiungere gli obiettivi dello sprint effettuando una _decomposizione in task_ degli item selezionati.

- **Daily Scrum**: meeting giornaliero di circa 15/30 minuti, esso viene svolto circa ogni giorno, dal Martedì al Sabato, e coinvolge tutti i membri del team.
  Lo scopo di questo meeting è quello di tenere aggiornato il team sullo stato di avanzamento del progetto, e di individuare eventuali difficoltà incontrate dai membri del team.

- **Sprint review**: meeting di revisione della durata di circa 1 ora, che viene svolto alla fine di ogni sprint, ovvero ogni fine settimana, e coinvolge tutti i membri del team.
  Lo scopo di questo meeting è quello di valutare il lavoro svolto durante lo sprint, e di individuare eventuali miglioramenti da apportare al processo di sviluppo.

## Trello

Per la realizzazione del Product Backlog e per la gestione delle attività da svolgere, è stato deciso di utilizzare _Trello_, che è uno strumento che agevola la gestione del progetto, basato su _Kanban_.

- Ad ogni Sprint Planning, vengono selezionati gli item dal Product Backlog che verranno sviluppati, e vengono creati i task necessari per lo sviluppo di ogni item tramite decomposizione.
- Per i task più importanti vengono assegnati dei membri del team responsabili dello sviluppo fin da subito, mentre quelli meno importanti vengono lasciati senza assegnatario e possono essere auto-assegnati dai membri del team che hanno già completato i propri task.

Sono state utilizzate le label di Trello per identificare le macro-aree ricoperte dai vari task, in particolare sono state identificate le seguenti:

- `C.I.`: Continuous Integration
- `Docs`: Documentazione
- `Scrum`: Processo di sviluppo
- `Code`: Implementazione di codice
- `Test`: Testing
- `Style`: Stile del codice
- `Architecture`: Architettura del software
- `Research`: Ricerca e/o studio di nuove tecnologie

Inoltre, anche per la gestione della stima della durata dei task sono state sfruttate le label di Trello, in particolare sono state identificate le seguenti **label**:

- `1h`: task che richiede 1 ora di lavoro
- `2h`: task che richiede 2 ore di lavoro
- `4h`: task che richiede 4 ore di lavoro
- `8h`: task che richiede 8 ore di lavoro
- `12h`: task che richiede 12 ore di lavoro

Nel caso in cui un task non rientri in nessuna delle stime sopra indicate, viene arrotondata per eccesso alla stima più vicina.

Inoltre, qualora un task richieda più di 12 ore di lavoro, viene suddiviso in più parti, in modo da avere task il più semplici possibili.

## DVCS

Per quanto riguarda l'utilizzo di _Git_ e _GitHub_, è stato deciso di adottare il modello _GitFlow_, che prevede l'utilizzo di diversi branch per la gestione del codice sorgente.

In particolare, è stato deciso di utilizzare i seguenti branch stabili:

- `main`: branch principale, contiene il codice stabile
- `doc`: branch per la documentazione del progetto, schemi, diagrammi, backlog ed altri artifatti
- `develop`: branch di sviluppo, contiene il codice in fase di sviluppo

Successivamente, è stato deciso di utilizzare la convezione di denominazione dei branch proposta da GitFlow, che prevede l'utilizzo dei seguenti prefissi:

- `feature-<featureName>`: branch per lo sviluppo di una feature
- `release-<releaseName>`: branch per la produzione di una release
- `hotfix-<hotfixName>`: branch per la correzione di un bug qualora si verifichi in produzione

### Commit message

Per la scrittura del messaggio di commit, è stato deciso di ispirarsi alla convenzione [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).

Principalmente sono stati previsti dei _prefissi_ per identificare il tipo di commit effettuato ed un _oggetto opzionale_ a cui si riferisce il commit, in aggiunta ad una descrizione testuale.

In particolare, è stato deciso di utilizzare i seguenti prefissi:

- `feat`: per la terminazione dell'oggetto
- `wip`: per il lavoro in corso sull'oggetto
- `ci`: per la modifica della configurazione di CI
- `fix`: per la correzione di un bug
- `docs`: per la modifica della documentazione
- `style`: per la modifica del codice che non impatta il comportamento del software
- `refactor`: per la modifica del codice che non impatta il comportamento del software
- `test`: per l'aggiunta e/o modifica di test
- `chore`: per piccole modifiche generali

### Pull Request

Il branch di develop, è stato protetto da push diretti, in modo da garantire che ogni modifica al codice sia effettuata tramite _Pull Request_.

Ogni pull request, deve essere approvata dagli altri due membri del team, prima di poter essere mergiata nel branch di develop.

## Definition of Done

Una feature è considerata finita ed integrabile in develop, se soddisfa i seguenti criteri:

- I test scritti per la feature passano tutti
- Gli altri test presenti nel progetto passano tutti
- È presente della documentazione del codice, qualora sia necessario per la comprensione da parte di chi effettua la revisione

## Testing

Per garantire la correttezza delle funzionalità sviluppate, è stato adottato l'approccio del _Test Driven Development_ (TDD). Quest'ultimo ci permette di identificare e correggere eventuali errori sui singoli componenti del software durante le fasi di sviluppo.

Il processo di sviluppo con TDD prevede tre passaggi principali:

1. **Scrittura di un Test**: inizialmente si scrive un test che descrive il comportamento desiderato del componente o della funzionalità che si sta sviluppando. Questo test deve fallire dato che il componente o la funzionalità non è stata ancora implementata.

2. **Implementazione**: successivamente, si procede con l'implementazione del componente o della funzionalità in modo da far superare il test precedentemente scritto.

3. **Refactoring**: dopo aver fatto passare il test, sarà possibile apportare eventuali miglioramenti al codice, refattorizzandolo per renderlo più pulito e meglio comprensibile. È importante che il test continui a passare anche dopo il refactoring.

### Test coverage

Per la misurazione della _code coverage_ nei test, è stato deciso di utilizzare [Scoverage](https://github.com/scoverage/sbt-scoverage), un plugin che permette di misurare la percentuale di codice coperto dai test.

## Building

Come build tool è stato scelto _Sbt_, che gestisce le opportune dipendenze del progetto.

Inoltre, facilita l'esecuzione dei test, la generazione della documentazione, compilazione e report del test coverage.

### CI/CD

Per la _Continuous Integration_ e _Continuous Deployment_ è stato scelto di utilizzare le GitHub Actions.

In particolare, sono stati definiti i seguenti _workflow_:

- **Pages deploy**: generazione dell'artefatto web e caricamento su GitHub Pages, che comprende:
  - [Il progetto](https://zucchero-sintattico.github.io/pps-22-scatan/)
  - [La documentazione](https://zucchero-sintattico.github.io/pps-22-scatan/docs/)
  - [Scaladoc](https://zucchero-sintattico.github.io/pps-22-scatan/scaladoc/)
  - [Test coverage](https://zucchero-sintattico.github.io/pps-22-scatan/test-coverage/)
- **Test & Format**: vengono eseguiti i test e il controllo del formato del codice

Questo strumento, in caso di fallimento, notifica via mail il membro del gruppo che ha iniziato l'esecuzione del workflow, in modo da poter intervenire tempestivamente qualora si verifichi un errore.

## Documentazione

Per quanto riguarda la documentazione del progetto, è stato utilizzato il linguaggio Markdown, scrivendola nell'apposito branch e, successivamente, viene caricata su GitHub Pages, in modo da renderla disponibile online.

Per la generazione è stato utilizzato il tool [mdBook](https://rust-lang.github.io/mdBook/) che permette di generare la documentazione in formato HTML.
