# Audit d'Architecture Zernex Video Player

## Pipeline de Rendu
1. **MediaCodec Hardware Acceleration**: Décodage direct sur le GPU via SurfaceView pour une consommation de batterie minimale.
2. **Gesture Controller**: Dispatcher d'événements tactiles avec zone morte centrale pour éviter les sauts involontaires.
3. **Audio Engine**: LoudnessEnhancer & GainNode virtuel pour suramplification jusqu'à 200% sans distorsion de saturation.
4. **Sous-titres ASS/SRT**: Moteur de rendu vectoriel pour styles et sous-titres synchronisés.
