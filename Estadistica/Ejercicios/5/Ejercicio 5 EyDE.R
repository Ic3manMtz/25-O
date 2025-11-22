# Ejercicios  5
#Datos

Marca1 <-c(86.92, 85.96, 86.47, 85.71, 86.26, 86.79)
Marca2 <-c(83.53, 84.36, 82.63, 82.94, 83.88, 83.21)
Marca3 <-c(83.88, 86.18, 84.87, 85.70, 85.21, 85.66)

#1)
boxplot(Marca1, Marca2, Marca3)

#2)
#Medias muestrales de cada tratamiento 
m1 = mean(Marca1)
m2 = mean(Marca2)
m3 = mean(Marca3)

m = 3
#3)Completar la tabla

Tratamientos <- c("1","1","1","1","1","1","2","2","2","2","2","2","3","3","3","3","3","3")
Insecticidas<-c(Marca1,Marca2,Marca3)

Datos<-data.frame(TRATAMIENTOS=Tratamientos,INSECTICIDA=Insecticidas)
Anova1<-aov(Datos$INS~Datos$TRAT)

summary(Anova1)

#4) alpha = 0.05 Rechaza la hip de anova
alpha = 0.05
sc.ERROR = 6.367
mc.ERROR = sc.ERROR/15 


abs(m1-m2)/sqrt(0.23*(1/4))>qtukey(0.95,3,9)
abs(m1-m3)/sqrt(0.23*(1/4))>qtukey(0.95,3,9)
abs(m3-m2)/sqrt(0.23*(1/4))>qtukey(0.95,3,9)



#5 y 6)
# Comparacion de medias
#(xi - xj)/ (CM. ERROR)*(1/n1+1/n2)
abs(m1-m2)/sqrt(mc.ERROR*(2/6))>qt(1-alpha/2, 15)
abs(m1-m3)/sqrt(mc.ERROR*(2/6))>qt(1-alpha/2, 15)
abs(m3-m2)/sqrt(mc.ERROR*(2/6))>qt(1-alpha/2, 15)