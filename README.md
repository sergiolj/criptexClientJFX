# Criptex Client (JavaFX)

Cliente gráfico do **Criptex**, um jogo de adivinhação de palavras de cinco letras no estilo *Wordle*.
A interface é feita em **JavaFX 21** e a comunicação com o servidor usa **Java RMI**.

> O servidor fica em outro repositório: [sergiolj/criptex-rmi](https://github.com/sergiolj/criptex-rmi).
> O cliente não funciona sozinho: o servidor precisa estar rodando e acessível pela rede.

## Sumário

- [Como o jogo funciona](#como-o-jogo-funciona)
- [Arquitetura](#arquitetura)
- [Requisitos para compilar](#requisitos-para-compilar)
- [Executar em modo de desenvolvimento](#executar-em-modo-de-desenvolvimento)
- [Gerar um executável que não precisa de JVM instalada](#gerar-um-executável-que-não-precisa-de-jvm-instalada)
  - [Passo 1: runtime enxuto com jlink](#passo-1-runtime-enxuto-com-jlink)
  - [Passo 2: executável nativo com jpackage](#passo-2-executável-nativo-com-jpackage)
  - [Passo 3 (opcional): instalador](#passo-3-opcional-instalador)
- [Configurar o endereço do servidor (`conf.cfg`)](#configurar-o-endereço-do-servidor-confcfg)
- [Solução de problemas](#solução-de-problemas)

## Como o jogo funciona

1. O jogador informa seu nome na tela inicial e clica em **Jogar**.
2. O cliente se conecta ao servidor RMI e registra o jogador (`loginUser`).
3. O jogador tem **6 tentativas** para descobrir a palavra secreta de **5 letras**.
4. A cada palpite, o servidor devolve o status de cada letra (posição certa, letra presente em outra posição ou letra ausente) e o teclado virtual é colorido de acordo.
5. Quem acerta entra no **ranking**, ordenado pelo tempo gasto.

A palavra secreta é definida pelo servidor e é a mesma para todos os jogadores conectados.

## Arquitetura

```
src/main/java
├── criptex/Launcher.java        # Ponto de entrada (classe Application do JavaFX)
├── controller/                  # Controllers das telas FXML (boas-vindas, jogo, ranking, regras)
├── view/manager/Navigation.java # Troca de telas no Stage principal
├── ui/component/                # Componentes visuais: teclado, painel de palavras, cronômetro, animações
├── rmi/network/GameSession.java # Sessão do jogo: conexão RMI, tentativas, estado da partida
├── model/Player.java            # Jogador (UUID, nome, pontuação)
├── shared/                      # Contrato compartilhado com o servidor
│   ├── remote/                  #   ServerInterface, ClientInterface, Ranking
│   ├── dto/                     #   GuessRequest, GuessResponse
│   └── config/Config.java       #   Leitura do arquivo conf.cfg (IP e porta do servidor)
└── util/AlertWindow.java
src/main/resources
├── view/*.fxml                  # Layouts das telas
├── styles/styles.css
├── fonts/                       # Orbitron e Press Start 2P (licença OFL)
└── img/
```

O pacote `shared` precisa ser **idêntico** no cliente e no servidor (mesmos nomes de classe, mesmos
`serialVersionUID`), pois os objetos trafegam serializados via RMI. O nome do serviço no registro RMI é
`CRIPTEX_RMI`.

O projeto é **modular** (`module-info.java`, módulo `criptex`). Isso é o que permite usar o `jlink`
para montar um runtime Java próprio, descrito abaixo.

## Requisitos para compilar

| Ferramenta | Versão | Observação |
|---|---|---|
| JDK | 21 ou superior | Precisa ser um **JDK** completo (tem `jlink` e `jpackage`), não apenas uma JRE. Ex.: Eclipse Temurin 21. |
| Maven | 3.8+ | Opcional: o projeto traz o Maven Wrapper (`mvnw` / `mvnw.cmd`). |
| JavaFX | 21.0.6 | Baixado automaticamente pelo Maven; não é preciso instalar o SDK do JavaFX. |

> Apenas **quem compila** precisa do JDK. Quem recebe o executável gerado não precisa ter Java instalado.

Nos exemplos abaixo, use `./mvnw` no Linux/macOS ou `mvnw.cmd` no Windows (ou `mvn`, se o Maven estiver
instalado).

## Executar em modo de desenvolvimento

Com o servidor já rodando:

```bash
./mvnw clean javafx:run
```

## Gerar um executável que não precisa de JVM instalada

A estratégia usa duas ferramentas que já vêm no JDK:

- **`jlink`** monta uma JVM reduzida contendo apenas os módulos que o jogo usa (`java.base`, `java.rmi`,
  `javafx.controls` etc.) junto com o próprio jogo. O resultado é uma pasta autocontida de ~100 MB.
- **`jpackage`** embrulha esse runtime em um executável nativo (`CriptexClient.exe` no Windows,
  `CriptexClient` no Linux, `CriptexClient.app` no macOS) e, se quiser, gera um instalador.

```
código-fonte ──mvn javafx:jlink──▶ target/criptex-client/  ──jpackage──▶ CriptexClient.exe (+ runtime embutido)
                                   (JVM enxuta + jogo)
```

> **Importante: gere o executável no mesmo sistema operacional em que ele vai rodar.** O `jlink` e o
> `jpackage` não fazem compilação cruzada, e o Maven baixa os binários nativos do JavaFX do sistema atual.
> Para distribuir para Windows, gere no Windows; para Linux, gere no Linux; e assim por diante.

### Passo 1: runtime enxuto com jlink

O `pom.xml` já está configurado com o `javafx-maven-plugin`, então basta:

```bash
./mvnw clean javafx:jlink
```

Resultado em `target/`:

| Caminho | Conteúdo |
|---|---|
| `target/criptex-client/` | Runtime completo: JVM reduzida + módulos JavaFX + o jogo |
| `target/criptex-client/bin/criptex` (`criptex.bat` no Windows) | Script que inicia o jogo usando a JVM embutida |
| `target/criptex-client.zip` | A mesma pasta compactada, pronta para distribuir |

Esse ZIP **já roda em uma máquina sem Java**: basta descompactar e executar `bin/criptex` (ou
`bin\criptex.bat` no Windows). Se um script de inicialização for suficiente, pode parar aqui.

A configuração usada pelo plugin (no `pom.xml`):

```xml
<configuration>
    <mainClass>criptex/criptex.Launcher</mainClass>   <!-- módulo/classe principal -->
    <launcher>criptex</launcher>                       <!-- nome do script em bin/ -->
    <jlinkZipName>criptex-client</jlinkZipName>
    <jlinkImageName>criptex-client</jlinkImageName>
    <noManPages>true</noManPages>                      <!-- remove páginas de manual -->
    <stripDebug>true</stripDebug>                      <!-- remove informações de debug -->
    <noHeaderFiles>true</noHeaderFiles>                <!-- remove headers C -->
</configuration>
```

Para conferir quais módulos entraram no runtime:

```bash
target/criptex-client/bin/java --list-modules
```

### Passo 2: executável nativo com jpackage

Com o runtime do passo 1 pronto, gere um executável de verdade (com ícone e sem janela de terminal).

**Windows (PowerShell ou CMD):**

```bat
jpackage --type app-image --name CriptexClient --dest target\instalador --runtime-image target\criptex-client --module criptex/criptex.Launcher --app-version 1.0.0 --vendor "github.com/sergiolj"
```

**Linux / macOS:**

```bash
jpackage --type app-image --name CriptexClient --dest target/instalador --runtime-image target/criptex-client --module criptex/criptex.Launcher --app-version 1.0.0 --vendor "github.com/sergiolj"
```

O que cada opção faz:

| Opção | Significado |
|---|---|
| `--type app-image` | Gera uma pasta com o executável (sem instalador). |
| `--name CriptexClient` | Nome do executável e da pasta gerada. |
| `--dest target/instalador` | Onde a saída é gravada. |
| `--runtime-image target/criptex-client` | Reaproveita o runtime gerado pelo jlink (por isso o jogo já está dentro dele). |
| `--module criptex/criptex.Launcher` | Módulo e classe principal, no formato `módulo/classe`. |
| `--app-version` | Versão exibida nas propriedades do executável. |
| `--icon <arquivo>` | (Opcional) Ícone: `.ico` no Windows, `.icns` no macOS, `.png` no Linux. |

Resultado:

- **Windows:** `target\instalador\CriptexClient\CriptexClient.exe`
- **Linux:** `target/instalador/CriptexClient/bin/CriptexClient`
- **macOS:** `target/instalador/CriptexClient.app`

Para distribuir, **copie a pasta `CriptexClient` inteira** (e não só o `.exe`): a JVM embutida fica na
subpasta `runtime/` e o executável depende dela. Compacte a pasta em um ZIP e envie.

> Nota: se o `jpackage` não for reconhecido no terminal, use o caminho completo do JDK, por exemplo
> `"C:\Program Files\Eclipse Adoptium\jdk-21...\bin\jpackage.exe"`, ou adicione a pasta `bin` do JDK ao
> `PATH`.

### Passo 3 (opcional): instalador

Trocando `--type app-image` por um tipo de instalador, o `jpackage` gera um instalador nativo que cria
atalhos no menu Iniciar/área de trabalho:

| Sistema | `--type` | Pré-requisito extra |
|---|---|---|
| Windows | `exe` ou `msi` | [WiX Toolset](https://wixtoolset.org/) instalado e no `PATH` |
| Linux | `deb` ou `rpm` | `dpkg-deb` (Debian/Ubuntu) ou `rpm-build` (Fedora) |
| macOS | `dmg` ou `pkg` | nenhum |

Exemplo para Windows, com atalhos e menu:

```bat
jpackage --type exe --name CriptexClient --dest target\instalador --runtime-image target\criptex-client --module criptex/criptex.Launcher --app-version 1.0.0 --vendor "github.com/sergiolj" --win-shortcut --win-menu --win-dir-chooser
```

## Configurar o endereço do servidor (`conf.cfg`)

Na primeira conexão, o cliente cria um arquivo `conf.cfg` no **diretório de trabalho atual** (a pasta de
onde o programa foi iniciado) com o conteúdo padrão:

```
127.0.0.1;1099
```

O formato é `IP;PORTA`. Para jogar contra um servidor em outra máquina, edite o arquivo com o IP do
servidor (por exemplo `192.168.0.10;1099`). O arquivo é relido automaticamente quando é alterado, sem
precisar reiniciar o jogo. O menu **Ajuda** mostra o servidor, IP e porta em uso.

## Solução de problemas

| Sintoma | Causa provável / solução |
|---|---|
| `Module ... does not have main class` ao rodar `javafx:jlink` | O `mainClass` no `pom.xml` deve usar o nome do módulo declarado no `module-info.java` (`criptex`), no formato `criptex/criptex.Launcher`. |
| `jlink`/`jpackage` não encontrado | Está usando uma JRE ou um JDK antigo. Instale um JDK 21+ e confira com `java -version` e `jpackage --version`. |
| Executável não abre em outro computador | Copie a pasta inteira gerada pelo jpackage (incluindo `runtime/`), não só o `.exe`. Confira também se ela foi gerada no mesmo sistema operacional. |
| "Não foi possível estabelecer conexão com o servidor" | O servidor não está rodando, o IP/porta do `conf.cfg` está errado ou um firewall bloqueia a porta 1099. |
| Erro de desserialização ou `ClassNotFoundException` vindo do RMI | O pacote `shared` está diferente entre cliente e servidor. Mantenha as classes idênticas nos dois projetos. |

## Autoria

Sérgio Lopes Júnior.
