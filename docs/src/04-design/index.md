# Design di dettaglio

## Organizzazione del codice

![Code organization](../img/04-design/scatan-packages.jpg)

Il codice è organizzato in 5 package principali:

- `model` contiene le classi che implementano la logica di business dell'applicazione, ovvero le regole del gioco e che permettono di gestire il suo svolgimento.
- `views` contiene le entità che permettono di visualizzare lo stato dell'applicazione all'utente e che ne permetto l'interazione diretta tramite un interfaccia grafica.
- `controllers` contiene le entità che permettono di gestire le interazioni dell'utente con l'applicazione, ovvero quelle che implementano la logica di accesso e aggiornamento del Model.
- `utils` contiene le classi di utilità, ovvero quelle che implementano funzionalità di supporto.
- `lib` contiene i module sviluppati che sono diventati parte fondamente del progetto.
  Tra questi troviamo:
  - `mvc` contentente le classi che implementano il pattern architetturale MVC scelto per il progetto e tutte le sue componenti.
  - `game` contentente le classi che implementano un engine di gioco generico, che può essere utilizzato per implementare giochi basati su fasi, step e azioni.
    All'interno di questo package troviamo anche il `dsl` che permette di definire un gioco in modo dichiarativo.

<!--************ MAZZOLI *****************-->

## Architettura

### Model

### Controller

### View

#### ViewModel

### Application

<!--************ MAZZOLI *****************-->

## Game Engine

### Game

### Rules

<!--************ MAZZOLI *****************-->

## DSL

### PropertiesDSL

### GameDSL

<!--************* ---- ****************-->

## Model

### Game

### Map

### Components

## View

### Components

### ViewModel
