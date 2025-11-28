# MODELOS CON M TRATAMIENTOS y 1 BLOQUE 
MA<-c(6,9,7,8)
MB<-c(7,10,11,8)
MC<-c(10,16,11,14)
MD<-c(10,13,11,9)

boxplot(MA,MB,MC,MD,col=c('red','forestgreen','blue','gold'))

Metodo<-rep(c('A','B','C','D'),4)
Operador<-rep(c('1','2','3','4'),each=4)
Tiempo<-c(6,7,10,10,
          9,10,16,13,
          7,11,11,11,
          8,8,14,9)

DatosEnsambles<-data.frame(METODO=Metodo,
                           OPERADOR=Operador,
                           TIEMPO=Tiempo)

AnovaEnsambles<-aov(TIEMPO~METODO*OPERADOR,
                    data=DatosEnsambles)

summary(AnovaEnsambles)

F0.TRAT<-20.5/2
p.valor1<-1-pf(F0.TRAT,3,9)

F0.BLOQ<-9.5/2
p.valor2<-1-pf(F0.BLOQ,3,9)

abs(mean(MA)-mean(MB))>qt(1-0.05/2,9)
abs(mean(MA)-mean(MC))>qt(1-0.05/2,9)
abs(mean(MA)-mean(MD))>qt(1-0.05/2,9)
abs(mean(MB)-mean(MC))>qt(1-0.05/2,9)
abs(mean(MB)-mean(MD))>qt(1-0.05/2,9)
abs(mean(MC)-mean(MD))>qt(1-0.05/2,9)

