# TEMA 5

Tratamiento1<-c(4.3,5.2,4.8,4.5)
Tratamiento2<-c(6.5,7.3,6.9,6.1)
Tratamiento3<-c(9,7.8,8.5,8.1)

boxplot(Tratamiento1,Tratamiento2,Tratamiento3,col=c('red','blue','green'))

m<-3
m1<-mean(Tratamiento1); m2<-mean(Tratamiento2); m3<-mean(Tratamiento3)
v1<-var(Tratamiento1); v2<-var(Tratamiento2); v3<-var(Tratamiento3)

n1<-4; n2<-4; n3<-4

N<-n1+n2+n3

mTot<-sum(Tratamiento1,Tratamiento2,Tratamiento3)/N

SC.Tratamientos<-4*sum((c(m1,m2,m3)-mTot)^2)
SC.Error<- ((n1-1)*v1+(n2-1)*v2+(n3-1)*v3)

CM.Tratamientos<-SC.Tratamientos/(m-1)
CM.Error <- SC.Error/(N-m)

F0<-CM.Tratamientos/CM.Error

alpha<-0.05

qf(1-alpha,m-1,N-m)
F0>qf(1-alpha,m-1,N-m)

p.valor<- 1-pf(F0,m-1,N-m)
p.valor<alpha

# aov

Tratamientos<-c("1","1","1","1","2","2","2","2","3","3","3","3")
Almidon<-c(Tratamiento1,Tratamiento2,Tratamiento3)

Datos<-data.frame(TRATAMIENTOS=Tratamientos,ALMIDON=Almidon)
Anova1<-aov(Datos$ALM~Datos$TRAT)

summary(Anova1)

# Comparacion de medias

abs(m1-m2)/sqrt(0.23*(2/4))>qt(1-0.05/2,9)
abs(m1-m3)/sqrt(0.23*(2/4))>qt(1-0.05/2,9)
abs(m3-m2)/sqrt(0.23*(2/4))>qt(1-0.05/2,9)

abs(m1-m2)/sqrt(0.23*(1/4))>qtukey(0.95,3,9)
abs(m1-m3)/sqrt(0.23*(1/4))>qtukey(0.95,3,9)
abs(m3-m2)/sqrt(0.23*(1/4))>qtukey(0.95,3,9)

# Verificacion supuestos

hist(Anova1$residuals)
qqnorm(Anova1$residuals)
qqline(Anova1$residuals)

shapiro.test(Anova1$residuals)

bartlett.test(Datos$ALM~Datos$TRAT)

plot(Anova1$residuals)
