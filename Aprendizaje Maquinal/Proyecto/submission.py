import pandas as pd
import numpy as np
import time
import os
import matplotlib.pyplot as plt

from sklearn.preprocessing import StandardScaler
from sklearn.impute import SimpleImputer
from sklearn.metrics import mean_squared_error
from sklearn.linear_model import Lasso

def configurar_hardware():
    os.environ["LOKY_MAX_CPU_COUNT"] = "20"

def cargar_y_procesar_datos():
    ruta = './hull-tactical-market-prediction/hull-tactical-market-prediction/train.csv'
    
    df = pd.read_csv(ruta)
    
    cols_a_borrar = ['E7', 'V10']
    df = df.drop(columns=[c for c in cols_a_borrar if c in df.columns], errors='ignore')
    
    target_col = 'market_forward_excess_returns'
    exclude_cols = ['date_id', 'forward_returns', 'risk_free_rate', 'market_forward_excess_returns']
    feature_cols = [c for c in df.columns if c not in exclude_cols]
    
    df = df.dropna(subset=[target_col])
    
    X = df[feature_cols]
    y = df[target_col]
    
    imputer = SimpleImputer(strategy='mean')
    scaler = StandardScaler()
    
    X_processed = imputer.fit_transform(X)
    X_scaled = scaler.fit_transform(X_processed)
    
    X_final = pd.DataFrame(X_scaled, columns=feature_cols)
    y_final = y.reset_index(drop=True)
    
    return X_final, y_final

def evaluar_estrategia(y_true, y_pred):
    posicion = np.clip(y_pred * 20, -1, 1)
    retornos = posicion * y_true
    if np.std(retornos) == 0: return 0.0
    return np.mean(retornos) / np.std(retornos)

def main():
    configurar_hardware()
    X, y = cargar_y_procesar_datos()
    
    corte = int(len(X) * 0.8)
    X_train, X_val = X.iloc[:corte], X.iloc[corte:]
    y_train, y_val = y.iloc[:corte], y.iloc[corte:]
    
    print(f"\nEntrenamiento: {len(X_train)} | Validacion: {len(X_val)}")
    print("-" * 100)
    print(f"{'MODELO':<30} | {'MSE':<10} | {'Sharpe':<10} | {'Volatilidad':<12} | {'Tiempo':<6}")
    print("-" * 100)
    
    nombre = "Lasso (Alpha=0.00001)"
    modelo = Lasso(alpha=0.00001, random_state=42)
    
    t0 = time.time()
    modelo.fit(X_train, y_train)
    preds = modelo.predict(X_val)
    t1 = time.time()
    
    mse = mean_squared_error(y_val, preds)
    sharpe = evaluar_estrategia(y_val, preds)
    std_preds = np.std(preds)
    
    print(f"{nombre:<30} | {mse:.6f}   | {sharpe:.6f}   | {std_preds:.6f}       | {t1-t0:.2f}s")
    print("-" * 100)
    
    posicion_simulada = np.sign(preds)
    retornos_estrategia = posicion_simulada * y_val
    
    acumulado_mercado = y_val.cumsum()
    acumulado_estrategia = retornos_estrategia.cumsum()
    
    plt.figure(figsize=(12, 6))
    
    plt.plot(acumulado_mercado.reset_index(drop=True), label='Mercado', color='gray', alpha=0.5, linestyle='--')
    plt.plot(acumulado_estrategia.reset_index(drop=True), label='Modelo Lasso', color='green', linewidth=2)
    
    plt.title(f'Backtest: Lasso vs Mercado')
    plt.xlabel('Dias de Trading')
    plt.ylabel('Retorno Acumulado')
    plt.legend()
    plt.grid(True)
    
    archivo_img = 'resultado_final_lasso.png'
    plt.savefig(archivo_img)
    print(f"Grafico guardado como: {archivo_img}")

if __name__ == "__main__":
    main()