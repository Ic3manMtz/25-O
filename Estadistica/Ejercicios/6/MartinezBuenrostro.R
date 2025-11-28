## Ejercicio 6

# Carga de datos
silo_A <- c(4.3, 4.0, 4.3, 3.8, 4.4)
silo_B <- c(4.4, 4.3, 3.4, 4.0, 3.8)
silo_C <- c(3.3, 3.8, 3.6, 3.4, 3.8)
silo_D <- c(6.6, 6.0, 6.2, 5.5, 5.6)
silo_E <- c(3.4, 4.4, 3.6, 3.8, 4.0)

# 1. Diagramas de caja
boxplot(silo_A,silo_B,silo_C,silo_D,silo_E,col=c('red','forestgreen','blue','gold','orange'))

# 2. Medidas importantes
Silo <- rep(c('A', 'B', 'C', 'D', 'E'), each = 5)
Dia <- rep(c('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes'), 5)
Temperatura <- c(4.3, 4.0, 4.3, 3.8, 4.4,  # Silo A
                 4.4, 4.3, 3.4, 4.0, 3.8,  # Silo B
                 3.3, 3.8, 3.6, 3.4, 3.8,  # Silo C
                 6.6, 6.0, 6.2, 5.5, 5.6,  # Silo D
                 3.4, 4.4, 3.6, 3.8, 4.0)  # Silo E

DatosSilos <- data.frame(SILO = as.factor(Silo),
                         DIA = as.factor(Dia),
                         TEMP = Temperatura)

m <- length(unique(DatosSilos$SILO))  # Número de tratamientos
b <- length(unique(DatosSilos$DIA))   # Número de bloques
n_i <- b  # Número de datos por tratamiento
N <- nrow(DatosSilos)  # Total de datos

cat("Número de tratamientos (silos), m =", m, "\n")
cat("Número de bloques (días), b =", b, "\n")
cat("Número de datos en cada tratamiento, n_i =", n_i, "\n")
cat("Número total de datos, N =", N, "\n\n")

mA <- mean(Silo_A); mB <- mean(Silo_B); mC <- mean(Silo_C)
mD <- mean(Silo_D); mE <- mean(Silo_E)

vA <- var(Silo_A); vB <- var(Silo_B); vC <- var(Silo_C)
vD <- var(Silo_D); vE <- var(Silo_E)
cat("Medias muestrales",mA,mB,mC,mD,mE)

# 3. ANOVA
anovaSilos <- aov(TEMP ~ SILO + DIA, data = DatosSilos)

summary(anovaSilos)

# 4. Decisión con alpha=0.01

# 5. Comparación LSD

