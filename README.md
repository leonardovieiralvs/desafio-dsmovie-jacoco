# DSMovie Jacoco 🎬🧪

Desafio de testes unitários proposto no curso **Java Spring Professional**, da [DevSuperior](https://devsuperior.com.br), do professor Nélio Alves.

Após um capítulo inteiro dedicado a testes, o objetivo do desafio era cobrir o projeto **DSMovie** com testes unitários na camada de serviço e acompanhar a cobertura pelo **JaCoCo** até chegar em 100%.

## O que foi feito

- Testes unitários da camada de serviço (`MovieService` e demais services do projeto)
- Cenários de sucesso e de exceção (`ResourceNotFoundException`, `DatabaseException`)
- Stubs com Mockito: `when/thenReturn`, `thenThrow`, `doNothing` e `doThrow` (para métodos `void`, como o `deleteById`)
- Factories para criar os objetos de teste de forma limpa e reaproveitável
- Acompanhamento da cobertura de código com o relatório do JaCoCo

## Tecnologias

- Java
- Spring Boot
- JUnit 5
- Mockito
- JaCoCo
- Maven

## Como executar os testes

```bash
mvn clean test
```

O relatório de cobertura do JaCoCo é gerado em:

```
target/site/jacoco/index.html
```

## Aprendizados

- Um teste que passa não garante que o teste está correto: o Mockito devolve `null` quando um stub não está bem configurado, então cada `when` precisa casar com o que o service realmente chama.
- Cobrir cenários de exceção é tão importante quanto cobrir o caminho feliz.
- Factories e um `@BeforeEach` bem organizado deixam os testes mais legíveis e fáceis de manter.
