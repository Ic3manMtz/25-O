## Ejercicio 6

# Carga de datos
Silo_A <- c(4.3, 4.0, 4.3, 3.8, 4.4)
Silo_B <- c(4.4, 4.3, 3.4, 4.0, 3.8)
Silo_C <- c(3.3, 3.8, 3.6, 3.4, 3.8)
Silo_D <- c(6.6, 6.0, 6.2, 5.5, 5.6)
Silo_E <- c(3.4, 4.4, 3.6, 3.8, 4.0)

# 1. Diagramas de caja
boxplot(Silo_A,Silo_B,Silo_C,Silo_D,Silo_E,col=c('red','forestgreen','blue','gold','orange'))

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

resumen <- summary(anovaSilos)

tabla_anova <- resumen[[1]]

# Sumas de cuadrados
SC_TRAT <- tabla_anova["SILO", "Sum Sq"]
SC_BLOQ <- tabla_anova["DIA", "Sum Sq"]
SC_ERROR <- tabla_anova["Residuals", "Sum Sq"]

# Grados de libertad
gl_TRAT <- tabla_anova["SILO", "Df"]
gl_BLOQ <- tabla_anova["DIA", "Df"]
gl_ERROR <- tabla_anova["Residuals", "Df"]

# Cuadrados medios
CM_TRAT <- tabla_anova["SILO", "Mean Sq"]
CM_BLOQ <- tabla_anova["DIA", "Mean Sq"]
CM_ERROR <- tabla_anova["Residuals", "Mean Sq"]

# Estadísticos F
F_TRAT <- tabla_anova["SILO", "F value"]
F_BLOQ <- tabla_anova["DIA", "F value"]

# p-valores
p_valor_TRAT <- 1-pf(F_TRAT,m-1,(m-1)*(b-1))
p_valor_BLOQ <- 1-pf(F_BLOQ,m-1,(m-1)*(b-1))

# 4. Decisión con alpha=0.01
alpha <- 0.01

cat("Para TRATAMIENTOS (Silos):\n")
if(p_valor_TRAT < alpha) {
  cat("  Decisión: RECHAZAMOS H0\n")
  cat("  Interpretación: Existe evidencia significativa de que al menos\n")
  cat("  un silo tiene temperatura promedio diferente a los demás.\n\n")
  rechazar_H0 <- TRUE
} else {
  cat("  Decisión: NO RECHAZAMOS H0\n")
  cat("  Interpretación: No hay evidencia suficiente para concluir\n")
  cat("  que existen diferencias entre las temperaturas de los silos.\n\n")
  rechazar_H0 <- FALSE
}

cat("Para BLOQUES (Días):\n")
if(p_valor_BLOQ < alpha) {
  cat("  Decisión: RECHAZAMOS H0\n")
  cat("  Interpretación: El día de la semana afecta significativamente\n")
  cat("  la temperatura registrada.\n\n")
} else {
  cat("  Decisión: NO RECHAZAMOS H0\n")
  cat("  Interpretación: El día de la semana no afecta significativamente\n")
  cat("  la temperatura registrada.\n\n")
}

# 5. Comparación LSD
alpha_lsd <- 0.05


# A vs B 
lAB <- abs(mA-mB) / sqrt(2*CM_ERROR/b)
# A vs C
lAC <- abs(mA-mC) / sqrt(2*CM_ERROR/b)
# A vs D
lAD <- abs(mA-mD) / sqrt(2*CM_ERROR/b)
# A vs E
lAE <- abs(mA-mE) / sqrt(2*CM_ERROR/b)
# B vs C
lBC <- abs(mB-mC) / sqrt(2*CM_ERROR/b)
# B vs D
lBD <- abs(mB-mD) / sqrt(2*CM_ERROR/b)
# B vs E
lBE <- abs(mB-mE) / sqrt(2*CM_ERROR/b)
# C vs D
lCD <- abs(mC-mD) / sqrt(2*CM_ERROR/b)
# C vs E
lCE <- abs(mC-mE) / sqrt(2*CM_ERROR/b)
# D vs E
lDE <- abs(mD-mE) / sqrt(2*CM_ERROR/b)

t.critico <- qt(1 - alpha/2, N-m)
cat("t critico:",t.critico)

cat("lAB:", lAB, "> t crítico?", lAB > t.critico, "\n")
cat("lAC:", lAC, "> t crítico?", lAC > t.critico, "\n")
cat("lAD:", lAD, "> t crítico?", lAD > t.critico, "\n")
cat("lAE:", lAE, "> t crítico?", lAE > t.critico, "\n")
cat("lBC:", lBC, "> t crítico?", lBC > t.critico, "\n")
cat("lBD:", lBD, "> t crítico?", lBD > t.critico, "\n")
cat("lBE:", lBE, "> t crítico?", lBE > t.critico, "\n")
cat("lCD:", lCD, "> t crítico?", lCD > t.critico, "\n")
cat("lCE:", lCE, "> t crítico?", lCE > t.critico, "\n")
cat("lDE:", lDE, "> t crítico?", lDE > t.critico, "\n")
