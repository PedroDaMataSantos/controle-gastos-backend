# Sistema de Controle de Gastos

Sistema desenvolvido para controle de gastos, receitas e investimentos pessoais/familiares, permitindo o cadastro, consulta, atualização, exclusão e acompanhamento de movimentações financeiras.

---

# Objetivo

O objetivo do sistema é permitir o gerenciamento simples de movimentações financeiras pessoais, separando registros entre receitas e gastos, controlando investimentos em renda fixa com cálculo automático de rendimento, e oferecendo uma visão consolidada através de um dashboard.

---

# Funcionalidades

## Cadastro de registros

Permite cadastrar:
- tipo de registro (ENTRADA ou SAIDA)
- categoria
- descrição
- valor
- data

## Consulta de registros

Permite:
- listar todos os registros
- buscar por ID
- buscar por categoria
- buscar por tipo
- buscar por descrição
- buscar por período

## Atualização e exclusão de registros

Permite editar e remover registros financeiros do sistema.

---

## Investimentos em Renda Fixa

Permite cadastrar investimentos por categoria (CDB, LCI, LCA, Poupança ou Outros), com cálculo automático de:

- **Juros compostos**, com taxa e periodicidade (mensal ou anual) definidas na criação
- **IOF regressivo**, seguindo a tabela oficial dos primeiros 30 dias de aplicação
- **Imposto de Renda**, por faixa de tempo (22,5% a 15%), com isenção automática para categorias isentas (LCI, LCA, Poupança)
- **Rendimento**, exposto em tempo real (calculado, não persistido) em cada investimento

## Saque de investimentos

Permite saque parcial, com:
- **Prévia de saque**: endpoint que retorna valor bruto, IOF, IR e valor líquido disponível antes de confirmar a operação
- Reaplicação do saldo remanescente, que segue rendendo a partir do saque

---

## Dashboard

Fornece uma visão consolidada com:
- Saldo, receitas, gastos e investimentos (totais e do mês atual)
- Rendimento total e mensal dos investimentos
- Patrimônio total (saldo + investido)
- Gastos e receitas agrupados por categoria (total e mensal)

---

# Regras de Negócio

- Campos obrigatórios devem ser preenchidos
- Categorias devem ser compatíveis com o tipo de registro
- Valores devem ser maiores que zero
- Taxa de juros e periodicidade são obrigatórias para investimentos que rendem (todas exceto "Outros")
- Isenção de IR é definida automaticamente pela categoria do investimento
- O valor originalmente aplicado é imutável após a criação; apenas o saldo (base de cálculo) muda após um saque
- Datas podem ser preenchidas automaticamente com a data atual

---

# Tecnologias Utilizadas

## Backend

- Java 21
- Spring Boot 4
- Spring Data JPA / Hibernate
- Bean Validation

## Banco de Dados

- SQLite

## Ferramentas e Bibliotecas

- Maven
- Lombok
- big-math (cálculo financeiro de alta precisão com BigDecimal)

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
- Cálculo financeiro com BigDecimal (juros compostos, IOF, IR)

---

# Status do Projeto

```text
Finalizado — V 1.5.1. concluída , calculos financeiros de rendimentos são realizados , e bugs da função investir corrigido
```

---

# Autor

Pedro da Mata Santos
