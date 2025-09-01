# Plataforma de Mentoria para Estudantes
## Visão Geral do Projeto
Este projeto consiste no desenvolvimento de uma "Plataforma de Mentoria para Estudantes", um sistema institucional projetado para conectar estudantes (Mentorados) com mentores experientes, que podem ser tanto estudantes mais avançados quanto profissionais formados. O objetivo é oferecer orientação acadêmica e de carreira, fortalecendo a comunidade institucional e potencializando o desenvolvimento dos alunos.


A plataforma é estruturada em torno de três perfis de utilizador: 

- Mentorado, Mentor e Administrador, cada um com funcionalidades específicas para atender às suas necessidades, garantindo segurança, integridade e eficácia.

## Tecnologias Utilizadas
O projeto foi desenvolvido utilizando as seguintes tecnologias:

### Backend:

- Java 17

- Spring Boot 3.x

- Spring Web

- Spring Data JPA

- Spring Security

- Hibernate

### Frontend:

- Thymeleaf

- Tailwind CSS

- Alpine.js

### Banco de Dados:

- H2 Database (para ambiente de desenvolvimento)

### Build e Gerenciamento de Dependências:

- Apache Maven

### Servidor de Aplicação:

- Apache Tomcat (embutido)

## Como Executar a Aplicação
Siga os passos abaixo para clonar, configurar e executar o projeto localmente.

### Pré-requisitos

- Java Development Kit (JDK) 17 ou superior.

- Apache Maven 3.9 ou superior.

- Git para clonar o repositório.

### Passos para Execução

1. Clone o Repositório:

Bash
```
git clone <URL_DO_REPOSITORIO>
cd plataforma-mentoria
```
2. Compile e Empacote o Projeto:
Utilize o Maven Wrapper (incluído no projeto) para garantir que está usando a versão correta do Maven.

-  No Windows:

Bash
```
./mvnw.cmd clean install
```
-  No Linux/macOS:

Bash
```
./mvnw clean install
```
Este comando irá baixar todas as dependências necessárias e compilar o código-fonte.

3. Execute a Aplicação:
Após a compilação bem-sucedida, você pode iniciar a aplicação Spring Boot com o seguinte comando:

-  No Windows:

Bash
```
./mvnw.cmd spring-boot:run
```
-  No Linux/macOS:

Bash
```
./mvnw spring-boot
```
4. Acesse a Aplicação:

- A aplicação estará disponível em: http://localhost:8080

- O console do banco de dados H2 pode ser acessado em: http://localhost:8080/h2-console

- JDBC URL: jdbc:h2:file:./data/mentoriadb

- User Name: sa

- Password: password

## Credenciais de Acesso Padrão

#### Para facilitar os testes, um usuário administrador é criado por padrão:

- Email: admin@plataforma.com

- Senha: admin123

## Documentação Complementar
A documentação do projeto é composta por artefatos que guiaram o seu desenvolvimento, incluindo o diagrama de classes, os wireframes das principais telas e a especificação detalhada de épicos e histórias de usuário.

### Diagrama de Classes

- O diagrama abaixo ilustra a estrutura das entidades do sistema e seus relacionamentos.

### Wireframes

- Os wireframes foram criados para visualizar a estrutura e o fluxo de navegação das principais páginas da plataforma.

-  Página de Login / Cadastro	Busca de Mentores	Painel do Mentorado
### Épicos e Histórias de Usuário

- A documentação completa dos requisitos funcionais e não funcionais, organizada em Épicos e Histórias de Usuário com seus respectivos Critérios de Aceitação, pode ser encontrada no documento:

docs/Plataforma de Mentoria_ Épicos e Requisitos.pdf
