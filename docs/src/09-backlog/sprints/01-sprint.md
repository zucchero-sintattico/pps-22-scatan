# Sprint 1 [28/08/2023 - 03/09/2023]

| Backlog Item                     | Id  | Task                                                           | Volontario         | Stima (h) | Effettivo (h) | D1 | D2 | D3 | D4 | D5 | D6 |
|:---------------------------------|:---:|:---------------------------------------------------------------|--------------------|:---------:|:-------------:|----|----|----|----|----|----|
| Setup repository                 | 1.1 | Capire Scala.js ed integrarlo nel progetto                     | Mazzoli, Borriello |     4     |       4       | 2  | 2  | 2  | 0  | 0  | 0  |
| Setup repository                 | 1.2 | Deploy su GitHub.io dell'applicazione                          | Borriello          |     1     |       1       | 1  | 1  | 0  | 0  | 0  | 0  |
| Setup repository                 | 1.3 | Setup del code style del progetto (findbugs, scalastyle)       | Mazzoli            |     2     |       2       | 0  | 0  | 0  | 0  | 0  | 0  |
| Setup repository                 | 1.4 | Setup scalatest & scoverage (deploy in ci)                     | Andruccioli        |     1     |       1       | 0  | 0  | 0  | 0  | 0  | 0  |
| Setup repository                 | 1.5 | Setup Doc con deploy su GitHub.io                              | Andruccioli        |     1     |       1       | 0  | 0  | 0  | 0  | 0  | 0  |
| Setup repository                 | 1.6 | Setup draw.io per diagrammi                                    | Mazzoli            |     1     |       1       | 0  | 0  | 0  | 0  | 0  | 0  |
| Setup repository                 | 1.7 | Setup scaladoc con deploy su GitHub.io                         | Andruccioli        |     1     |       1       | 1  | 0  | 0  | 0  | 0  | 0  |
| Domain understanding & reporting | 2.1 | Definizione dominio in UML                                     | Team               |     4     |       4       | 4  | 3  | 3  | 3  | 0  | 0  |
| Domain understanding & reporting | 2.2 | Completare documentazione del dominio con diagrammi esportati  | Team               |     4     |       4       | 4  | 4  | 4  | 4  | 2  | 0  |
| Domain understanding & reporting | 2.3 | Scrivere requisiti nella documentazione                        | Team               |     2     |       2       | 2  | 2  | 2  | 2  | 0  | 0  |
| Domain understanding & reporting | 2.4 | Scrivere introduzione documentazione con spiegazione del gioco | Team               |     1     |       1       | 1  | 1  | 1  | 1  | 0  | 0  |
| Domain understanding & reporting | 2.5 | Scrivere processo adottato nella relazione                     | Team               |     2     |       2       | 2  | 2  | 2  | 2  | 0  | 0  |
| Architettura dell'applicativo    | 3.1 | Analisi dei pro e contro nei pattern architetturali            | Mazzoli            |     4     |       4       | 4  | 4  | 4  | 2  | 0  | 0  |
| Architettura dell'applicativo    | 3.2 | Implementazione architettura base                              | Mazzoli            |     2     |       2       | 2  | 2  | 2  | 2  | 2  | 0  |
|                                  |     | **TOT**                                                        |                    |    30     |      30       | 23 | 21 | 20 | 16 | 4  | 0  |

## Sprint goal

Gli obiettivi di questo Sprint sono stati:

- Inizializziazione del progetto con relative configurazioni + C.I.
- Definizione di un primo dominio
- Definizione dell'architettura e prima implementazione

## Deadline

03/09/2023

## Sprint review

Lo Sprint goal è stato raggiunto secondo i tempi previsti.

## Sprint retrospective

Il primo sprint è stato incentrato sulla configurazione del progetto, che ha richiesto più tempo del previsto, data la
scelta delle tecnologie.

La poca configurabilità di Scala.js ha inficiato alle dipendenze del progetto utilizzata nell'IDE relativa alla parte
dei test. Infatti, l'utilizzo delle librerie compilate per
Javascript `("org.scalatest" %%% "scalatest" % "<version>" % Test)`, rendono inutilizzabile il pulsante run dei test,
poichè non è possibile eseguire codice compilato per js su JVM. A fronte di ciò è stato necessario inserire anche la
versione per JVM di scalatest, al fine di eseguire i test tramite l'IDE.

Inoltre anche la configurazione della C.I. ha richiesto parecchio tempo, questo a causa dei tempi elevati richiesti dal
deploy delle GitHub Actions, poichè si occupano di effettuare il deploy sia dell'applicazione, sia della documentazione
ma anche dello stile del codice, dei test e della coverage.