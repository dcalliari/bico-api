# Setup do Projeto - Java 21 com `mise`

## Instalação

1. Instale mise (se não tiver):
```bash
curl https://mise.jdx.dev/install.sh | sh
```

2. Adicione ao `~/.bashrc`:
```bash
eval "$(~/.local/bin/mise activate bash)"
```

3. Recarregue:
```bash
source ~/.bashrc
```

## Use

```bash
cd bico-api
./mvnw spring-boot:run
```

Pronto. O Java 21 é ativado automaticamente pelo `mise.toml`.



