# DailySync

Aplicação Android (DAM) para gestão de compromissos (criar, listar, editar e eliminar), com persistência via API REST própria (Node.js + Express) e suporte opcional de localização GPS.

## Curso / Disciplina / Ano letivo
- Curso: Engenharia Informática
- Disciplina: Desenvolvimento de Aplicações Móveis (DAM)
- Ano letivo: 2025/2026

## Autor
- Nº: 24706
- Nome: Bruno Alves

## Funcionalidades
- Login (credenciais: manel / 1234)
- Listagem de compromissos (RecyclerView)
- Adicionar compromisso
- Editar compromisso
- Eliminar compromisso
- Seleção de data com DatePicker
- Localização opcional:
  - manual (texto)
  - automática via GPS (botão "Obter Localização GPS")
- Página "Sobre" acessível no ecrã principal

## Tecnologias / Bibliotecas / Código de terceiros
- Retrofit (Square) – consumo de API REST
- Gson – conversão JSON
- RecyclerView – listagem de dados
- Material Components – UI (FAB e Cards)
- Google Play Services Location – obtenção de localização (FusedLocationProviderClient)
- Node.js + Express – API REST própria
- ngrok – exposição temporária da API para testes fora de localhost

## Como executar a API (Node.js)
1) Abrir PowerShell na pasta da API (ex: `dailysync-api`)
2) Instalar dependências (se necessário):
  - `npm i`
3) Iniciar a API:
  - `node index.js`
4) Testar no browser:
  - `http://localhost:3000/compromissos`
## Usar ngrok (para testar fora do PC)
1) Iniciar ngrok:
  - `ngrok http 3000`
2) Copiar o URL HTTPS gerado (ex: `https://xxxx.ngrok-free.app`)
3) No Android, atualizar a `BASE_URL` no `RetrofitClient.kt` para:
  - `https://xxxx.ngrok-free.app/`
    (tem de terminar com `/`)

## Notas
- Sempre que o ngrok reinicia, o URL pode mudar. Nessa situação é necessário atualizar a `BASE_URL` novamente.
- Em emulador, a localização pode exigir definição manual nas Extended Controls -> Location.
