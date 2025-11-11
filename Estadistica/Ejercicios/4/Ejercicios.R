# Ejercicio 1
# Carga de datos

datos <- c(3.33, 3.35, 3.11, 3.48, 2.87, 3.10, 3.32, 2.99, 3.30, 2.63, 
              3.10, 3.37, 2.95, 3.02, 2.93, 2.77, 3.05, 3.26, 3.16, 2.96, 
              2.85, 2.96, 3.06, 2.93, 3.14)


# a) Estimador para la media y el estimador para la varianza

media <- mean(datos)
varianza <- var(datos)

# b) Intervalo de 99% de confianza para la media
n <- length(datos)
alpha <- 0.01
qt <- qt(1-alpha/2,df=n-1)
cota_inferior <- media - (qt*sqrt(varianza/n))
cota_superior <- media + (qt*sqrt(varianza/n))

print(paste("IC 99% para media:", 
            round(cota_inferior, 4), "a", round(cota_superior, 4)))


# c) Intervalo de 95% de confianza para la varianza
alpha <- 0.05
qchi_inf <- qchisq(alpha/2,df=n-1)
qchi_sup <- qchisq(1-alpha/2,df=n-1)

cota_inferior <- (n-1)*varianza/qchi_inf
cota_superior <- (n-1)*varianza/qchi_sup

print(paste("IC 95% para varianza:", 
            round(cota_inferior, 4), "a", round(cota_superior, 4)))


# d) Por norma la leche entera debe tener al menos 3 gramos de proteína por
# cada 100 ml. Plantee y resuelva una prueba de hipótesis para tomar una 
# decisión sobre el proceso de producción

# 1. Hipótesis
# Ho : μ >= 3
# Ha : μ < 3
μ0 <- 3

# 2. Estadístico de prueba
t0 <- (media-μ0)/(sqrt(varianza/n))

# 3. Criterio de rechazo
# Se rechaza Ho si t0 < -t(alpha),n-1
t <- -qt(1-alpha,n-1)

print(paste("¿t0 =", round(t0,4)," < ",round(t,4),"?"))

# No, por lo tanto no se rechaza Ho, lo que significa que no hay evidencia
# estadística que indique que μ < 3

# e) La cantidad de proteína no debe variar más de 0.1 gramos, en caso contrario
# se debe ajustar le proceso de producción. Plantee y resuelva una prueba de
#hipótesis para tomar una decisión sobre el proceso de producción





# Ejercicio 2
# Carga de datos

datos <- c(8.4, 7.1, 13.1, 12.3, 11.2, 7.5, 6.7, 11.7, 8.0, 16.3, 
           10.0, 11.2, 9.8, 12.7, 13.2, 13.5, 8.0, 9.4, 11.8, 10.7, 
           10.8, 11.2, 9.7, 9.7, 9.6, 7.8, 7.8, 12.1, 6.6, 10.7, 
           11.2, 10.8, 6.5, 9.4, 13.9, 8.9, 8.1, 8.5, 10.9, 8.7, 
           11.4, 11.3, 10.0, 11.3, 13.4, 9.7, 8.0, 8.7, 8.7, 9.2)


# a) Estimador para la media y el estimador para la varianza
media <- mean(datos)
varianza <- var(datos)


# b) Intervalo de 99% de confianza para la media
n <- length(datos)
alpha <- 0.01
qt <- qt(1-alpha/2,df=n-1)
cota_inferior <- media - (qt*sqrt(varianza/n))
cota_superior <- media + (qt*sqrt(varianza/n))

print(paste("IC 99% para media:", 
            round(cota_inferior, 4), "a", round(cota_superior, 4)))


# c) Intervalo de 95% de confianza para la varianza
alpha <- 0.95
qchi_inf <- qchisq(alpha/2,df=n-1)
qchi_sup <- qchisq(1-alpha/2,df=n-1)

cota_inferior <- (n-1)*varianza/qchi_inf
cota_superior <- (n-1)*varianza/qchi_sup

print(paste("IC 95% para varianza:", 
            round(cota_inferior, 4), "a", round(cota_superior, 4)))


# d) El plomo (Pb) es un elemento que no resulta de utilidad para el organismo
# del ser humano. Sin embargo, se considera aceptable hasta 10 mcg/dL. Plantee
# y resuelva una prueba de hipótesis para verificar si la población está en el
# límite aceptable






# e) El gobierno se plantea el siguiente escenario: Si el nivel de plomo es
# menor a 10 y varía menos que 1.5 considerará que no hay una problema de 
# salud. Plantee las pruebas de hipótesis necesarias para tomar una 
# decisión sobre el estado de la salud en la población









