# Documentação do Sistema de E-commerce Multiusuário

## 1. Descrição Geral do Sistema

O sistema é uma **plataforma de e-commerce** que permite a interação entre **Clientes** e **Vendedores**, suportando funcionalidades completas de compra, venda e gerenciamento de produtos.

Principais características:
- Cadastro e login de usuários com tipos distintos (Cliente, Vendedor).
- Controle de acesso baseado no tipo de usuário.
- Criação e gerenciamento de lojas pelos vendedores.
- Compra de produtos, carrinho e histórico de pedidos para clientes.
- Integração com gateways de pagamento.
- Notificações em tempo real sobre pedidos e status.
- Segurança avançada e boas práticas (hashing de senhas, proteção contra ataques).

---

## 2. Casos de Uso

| Caso de Uso | Ator | Descrição |
|-------------|------|-----------|
| Cadastro de usuário | Cliente/Vendedor | Permite criar conta com validação de email. |
| Login | Cliente/Vendedor | Autentica o usuário com email e senha. |
| Reset de senha | Cliente/Vendedor | Permite redefinir senha via email. |
| Gerenciar loja | Vendedor | Criação, edição e remoção da loja. |
| Gerenciar produtos | Vendedor | Cadastro, edição e remoção de produtos. |
| Visualizar pedidos da loja | Vendedor | Lista pedidos realizados pelos clientes. |
| Estatísticas da loja | Vendedor | Visualiza vendas, produtos mais vendidos e estoque. |
| Navegar produtos | Cliente | Pesquisa por nome, categoria ou loja. |
| Adicionar ao carrinho | Cliente | Inclui produtos no carrinho para compra. |
| Finalizar compra | Cliente | Processa pagamento via gateway e registra pedido. |
| Histórico de pedidos | Cliente | Consulta status e detalhes de pedidos anteriores. |
| Avaliar produto/loja | Cliente | Deixa avaliações e comentários sobre produtos e lojas. |
| Receber notificações | Cliente/Vendedor | Alertas sobre novos pedidos ou atualizações de status. |

---

## 3. Modelos de Dados

### Diagrama Entidade-Relacionamento (ERD)

## Usuário
```
id
nome
email
senha_hash
tipo_usuario (Cliente/Vendedor)
data_criacao
```
## Loja
```
id
nome
descricao
vendedor_id (FK -> Usuário.id)
data_criacao
```
## Produto
```
id
nome
descricao
preco
quantidade
imagens
loja_id (FK -> Loja.id)
data_criacao
```
## Pedido
```
id
cliente_id (FK -> Usuário.id)
loja_id (FK -> Loja.id)
status (Pendente, Processando, Enviado, Entregue, Cancelado)
total
data_criacao
```
## ItemPedido
```
id
pedido_id (FK -> Pedido.id)
produto_id (FK -> Produto.id)
quantidade
preco_unitario
```
## Pagamento
```
id
pedido_id (FK -> Pedido.id)
metodo (Cartao, Boleto, Carteira Digital)
status (Pendente, Confirmado, Falhou)
valor
data_pagamento
```
## Avaliacao
```
id
usuario_id (FK -> Usuário.id)
produto_id (FK -> Produto.id) NULL
loja_id (FK -> Loja.id) NULL
nota (1-5)
comentario
data_criacao
```
---

## 4. Fluxos Principais

### 4.1 Fluxo de Autenticação

1. Usuário envia **email** e **senha** para `/login`.
2. Backend verifica credenciais.
3. Se válidas, gera **JWT** ou token de sessão.
4. Usuário recebe token e tem acesso aos recursos de acordo com seu tipo.
5. Reset de senha: usuário solicita via `/reset-password`, recebe email com token de redefinição.

### 4.2 Fluxo de Compra

1. Cliente adiciona produtos ao **carrinho**.
2. Cliente finaliza pedido enviando dados para `/checkout`.
3. Sistema valida estoque e calcula total.
4. Integração com gateway de pagamento.
5. Após confirmação, pedido é registrado e status atualizado.
6. Notificação enviada ao vendedor sobre novo pedido.
7. Cliente acompanha status do pedido até a entrega.

---

## 5. Endpoints da API

### Autenticação

| Endpoint | Método | Parâmetros | Resposta |
|----------|--------|-----------|----------|
| `/register` | POST | `{nome, email, senha, tipo_usuario}` | `201 Created` ou `400 Bad Request` |
| `/login` | POST | `{email, senha}` | `{token, usuario}` |
| `/reset-password` | POST | `{email}` | `200 OK` |
| `/verify-email` | GET | `?token=` | `200 OK` |

### Vendedor - Loja e Produtos

| Endpoint | Método | Parâmetros | Resposta |
|----------|--------|-----------|----------|
| `/lojas` | POST | `{nome, descricao}` | `201 Created` |
| `/lojas/{id}` | PUT | `{nome, descricao}` | `200 OK` |
| `/lojas/{id}` | DELETE | — | `204 No Content` |
| `/produtos` | POST | `{nome, descricao, preco, quantidade, imagens, loja_id}` | `201 Created` |
| `/produtos/{id}` | PUT | `{nome, descricao, preco, quantidade, imagens}` | `200 OK` |
| `/produtos/{id}` | DELETE | — | `204 No Content` |
| `/lojas/{id}/pedidos` | GET | — | Lista de pedidos da loja |
| `/lojas/{id}/estatisticas` | GET | — | `{vendas_totais, produtos_mais_vendidos, estoque}` |

### Cliente - Produtos e Compras

| Endpoint | Método | Parâmetros | Resposta |
|----------|--------|-----------|----------|
| `/produtos` | GET | `?nome=&categoria=&loja=` | Lista de produtos filtrados |
| `/carrinho` | GET | — | Lista de itens no carrinho |
| `/carrinho` | POST | `{produto_id, quantidade}` | `201 Created` |
| `/carrinho/{id}` | DELETE | — | Remove item |
| `/checkout` | POST | `{metodo_pagamento}` | `{pedido_id, status}` |
| `/pedidos` | GET | — | Lista de pedidos do cliente |
| `/pedidos/{id}` | GET | — | Detalhes do pedido |
| `/avaliacoes` | POST | `{produto_id?, loja_id?, nota, comentario}` | `201 Created` |

### Pagamentos

| Endpoint | Método | Parâmetros | Resposta |
|----------|--------|-----------|----------|
| `/pagamentos` | POST | `{pedido_id, metodo}` | `{status, transacao_id}` |
| `/pagamentos/{id}` | GET | — | `{status, valor, metodo}` |

---
