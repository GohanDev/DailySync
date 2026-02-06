# DailySync

## Descrição
O **DailySync** é uma aplicação Android desenvolvida no âmbito da unidade curricular, com o objetivo de permitir ao utilizador gerir compromissos diários de forma simples e intuitiva.

A aplicação possibilita a criação, visualização e persistência de compromissos, garantindo que os dados permanecem guardados entre execuções da aplicação.

---

## Funcionalidades
- Adicionar compromissos com título e data
- Listar compromissos numa interface organizada
- Armazenamento local dos dados
- Interface simples e funcional
- Persistência de dados entre sessões

---

## Tecnologias Utilizadas
- **Android Studio**
- **Kotlin**
- **RecyclerView**
- **SharedPreferences**
- **Gson**
- **Gradle (Kotlin DSL)**

---

## Estrutura do Projeto
- `MainActivity`  
  Responsável pela lógica principal da aplicação e interação com o utilizador.

- `Compromisso`  
  Classe de dados que representa um compromisso.

- `CompromissoAdapter`  
  Adapter utilizado para apresentar os compromissos numa `RecyclerView`.

---

## Armazenamento de Dados
Os compromissos são guardados localmente utilizando **SharedPreferences**, sendo convertidos para JSON através da biblioteca **Gson**.

---

## Objetivo Académico
Este projeto foi desenvolvido com fins académicos, cumprindo os requisitos definidos no enunciado fornecido pelo docente.

---

## Autor
Bruno Alves