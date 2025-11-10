# mee6-payments-jee — README

Projeto base **Jakarta EE 10 + MicroProfile 6** rodando no **Payara Micro 6.2025.10**.  
Foco: **resiliência**, **observabilidade** e um **esqueleto limpo** para evoluir um serviço de pagamentos.

> Execução **com e sem Docker**, caminhos de endpoints, expectativas de resposta, configurações e troubleshooting inicial.

---

## Sumário

- [mee6-payments-jee — README](#mee6-payments-jee--readme)
  - [Sumário](#sumário)
  - [Arquitetura \& tecnologia](#arquitetura--tecnologia)
  - [Pré-requisitos](#pré-requisitos)
  - [Estrutura do projeto](#estrutura-do-projeto)
  - [Builds e modos de execução](#builds-e-modos-de-execução)
    - [A) Sem Docker (recomendado para dev local)](#a-sem-docker-recomendado-para-dev-local)
    - [B) Com Docker (execução containerizada)](#b-com-docker-execução-containerizada)
  - [Endpoints \& verificações](#endpoints--verificações)
  - [Configuração (MicroProfile Config)](#configuração-microprofile-config)
  - [Logs \& observabilidade](#logs--observabilidade)
  - [Pontos de atenção](#pontos-de-atenção)
  - [Troubleshooting](#troubleshooting)
  - [Próximos passos sugeridos](#próximos-passos-sugeridos)

---

## Arquitetura & tecnologia

- **Runtime**: Payara Micro 6.2025.10 (Jakarta EE 10)
- **APIs**: Jakarta EE (JAX-RS, CDI, etc.) + **MicroProfile** (OpenAPI, Metrics, Health, Fault Tolerance)
- **Empacotamento**: `war` + opção de **microbundle** (um único `.jar` executável)
- **Observabilidade**: endpoints padrão `/openapi`, `/metrics`, `/health`
- **Segurança**: base pronta para File Realm (opcional; pode ser ativada depois)
- **Java**: 17

---

## Pré-requisitos

- **Java 17** (JDK) no `PATH`  
  Verifique: `java -version`
- **Maven 3.8+**  
  Verifique: `mvn -version`
- (Opcional) **Docker Desktop** / daemon Docker rodando, caso queira usar o perfil `docker`

---

## Estrutura do projeto

```
mee6-payments-jee/
├─ pom.xml
├─ Dockerfile
├─ src/ 
│  ├─ main/java/
│  │  ├─ br/com/mee6/paymentsjee/resource/
│  │  │  ├─ HelloWorldResource.java
│  │  │  └─ PaymentResource.java
│  │  └─ br/com/mee6/paymentsjee/RestConfiguration.java
│  ├─ main/resources/
│  │  └─ META-INF/microprofile-config.properties
│  └─ main/webapp/
│     └─ swagger/index.html
└─ target/
```

---

## Builds e modos de execução

### A) Sem Docker (recomendado para dev local)

1. **Build padrão**
   ```bash
   mvn -U clean package
   ```

2. **Executar com Payara Micro**
   ```bash
   java -jar ~/.m2/repository/fish/payara/extras/payara-micro/6.2025.10/payara-micro-6.2025.10.jar    --autoBindHttp --contextRoot / --deploy target/mee6-payments-jee.war
   ```

3. **Gerar microbundle**
   ```bash
   mvn -U clean package -Pmicro-bundle
   java -jar target/mee6-payments-jee-microbundle.jar
   ```

---

### B) Com Docker (execução containerizada)

1. **Build da imagem**
   ```bash
   mvn -U clean package -Pdocker
   ```

2. **Rodar o container**
   ```bash
   docker run --rm -p 8080:8080 mee6-payments-jee:0.1.0-SNAPSHOT
   ```

---

## Endpoints & verificações

- `/api/hello` — teste básico
- `/api/payments` — criar/consultar pagamentos
- `/openapi` — contrato OpenAPI (JSON)
- `/metrics` — métricas (Prometheus)
- `/health` — health check JSON

---

## Configuração (MicroProfile Config)

`src/main/resources/META-INF/microprofile-config.properties`:

```properties
mp.openapi.extensions.enabled=true
mp.metrics.appName=mee6-payments-jee
mp.fault.tolerance.default.timeout.duration=800
```

---

## Logs & observabilidade

- Logs via stdout (ou `docker logs <id>`)
- Métricas acessíveis via `/metrics`
- Health checks em `/health`
- Documentação OpenAPI em `/openapi`

---

## Pontos de atenção

1. O build padrão **não requer Docker nem plugin do Payara**.
2. Portas: `--autoBindHttp` evita conflito.  
3. Use perfis `-Pmicro-bundle` ou `-Pdocker` conforme necessidade.
4. Segurança ainda **não ativada** (padrão: livre).

---

## Troubleshooting

- **NPE no payara-micro plugin** → use `-Pmicro-bundle` ou rode Payara manualmente.
- **Erro de Docker** → certifique-se de que o Docker está ativo ou use build padrão.
- **Artefatos payara-p1 não encontrados** → `mvn -U clean package`.

---

## Próximos passos sugeridos

1. Adicionar JPA + H2 e `persistence.xml`.
2. Implementar idempotência nos pagamentos.
3. Adicionar métricas de negócio e tracing.
4. Integrar autenticação (JWT / File Realm).
