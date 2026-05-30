# Sistema de Controle de Gastos

Sistema desenvolvido para controle de gastos e receitas pessoais, permitindo o cadastro, consulta, atualização e exclusão de registros financeiros.

---

# Objetivo

O objetivo do sistema é permitir o gerenciamento simples de movimentações financeiras pessoais, separando registros entre receitas e gastos, com categorização e validações de regras de negócio.

---

# Funcionalidades

## Cadastro de registros
Permite cadastrar:
- tipo de registro (RECEITA ou GASTO)
- categoria
- descrição
- valor
- data

---

## Consulta de registros
Permite:
- listar todos os registros
- buscar por ID
- buscar por categoria
- buscar por tipo
- buscar por período

---

## Atualização de registros
Permite editar:
- tipo
- categoria
- descrição
- valor
- data

---

## Exclusão de registros
Permite remover registros financeiros do sistema.

---

# Regras de Negócio

- Campos obrigatórios devem ser preenchidos
- Categorias devem ser compatíveis com o tipo de registro
- Valores devem ser maiores que zero
- O sistema diferencia registros entre RECEITA e GASTO
- Datas podem ser preenchidas automaticamente com a data atual

---

# Tecnologias Utilizadas

## Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate

## Banco de Dados
- PostgreSQL

## Ferramentas
- Maven
- Lombok

---

# Arquitetura

O sistema segue arquitetura em camadas:

```text
Controller → Service → Repository → Banco de Dados
```

---

# Conceitos Aplicados

- Programação Orientada a Objetos (POO)
- Injeção de Dependência
- CRUD
- Validações de Regras de Negócio
- Enumerações
- Persistência com JPA
- Arquitetura REST

---

# Status do Projeto

```text
Em desenvolvimento
```

---

# Autor

Pedro da Mata Santos
