## Ejercicio 5

# Carga de datos

Marca1 <- c(86.92, 85.96, 86.47, 85.71, 86.26, 86.79)
Marca2 <- c(83.53, 84.36, 82.63, 82.94, 83.88, 83.21)
Marca3 <- c(83.88, 86.18, 84.87, 85.70, 85.21, 85.66)

# 1. Diagramas de caja
boxplot(Marca1, Marca2, Marca3, 
        names = c("Marca1", "Marca2", "Marca3"),
        col = c("red", "blue", "green"),
        main = "Diagramas de caja de efectividad por marca")

# 2. Medidas importantes
m <- 3
n1 <- length(Marca1); n2 <- length(Marca2); n3 <- length(Marca3)
N <- n1 + n2 + n3

m1 <- mean(Marca1); m2 <- mean(Marca2); m3 <- mean(Marca3)
v1 <- var(Marca1); v2 <- var(Marca2); v3 <- var(Marca3)

cat("Número de tratamientos (m):", m, "\n")
cat("Número de datos por tratamiento:", n1, n2, n3, "\n")
cat("Número total de datos (N):", N, "\n")
cat("Medias muestrales:", m1, m2, m3, "\n")

# 3. ANOVA
Tratamientos <- c("1","1","1","1","1","1","2","2","2","2","2","2","3","3","3","3","3","3")
Insecticidas<-c(Marca1,Marca2,Marca3)

Datos<-data.frame(TRATAMIENTOS=Tratamientos,INSECTICIDA=Insecticidas)
Anova1<-aov(Datos$INS~Datos$TRAT)

summary(Anova1)

# 4. Decisión con alpha=0.05
alpha <- 0.05

SC.Tratamientos <- n1*(m1 - mean(c(Marca1, Marca2, Marca3)))^2 +
  n2*(m2 - mean(c(Marca1, Marca2, Marca3)))^2 +
  n3*(m3 - mean(c(Marca1, Marca2, Marca3)))^2

SC.Error <- (n1-1)*v1 + (n2-1)*v2 + (n3-1)*v3

CM.Tratamientos <- SC.Tratamientos / (m-1)
CM.Error <- SC.Error / (N-m)
F0 <- CM.Tratamientos / CM.Error

F.critico <- qf(1 - alpha, m-1, N-m)

cat("F crítico:", F.critico, "\n")
cat("¿Rechazar H0?:", F0 > F.critico, "\n")
cat("p-valor < alpha?:", p.valor < alpha, "\n")


# 5. Comparación LSD
# Usando CM.Error del ANOVA
CME <- CM.Error

# Comparaciones por pares
l12 <- abs(m1 - m2) / sqrt(CME * (1/n1 + 1/n2))
l13 <- abs(m1 - m3) / sqrt(CME * (1/n1 + 1/n3))
l23 <- abs(m2 - m3) / sqrt(CME * (1/n2 + 1/n3))

t.critico <- qt(1 - alpha/2, N-m)

cat("l12:", l12, "> t crítico?", l12 > t.critico, "\n")
cat("l13:", l13, "> t crítico?", l13 > t.critico, "\n")
cat("l23:", l23, "> t crítico?", l23 > t.critico, "\n")
