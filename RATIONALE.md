# Design Rationale — Bridge and Adapter Together

## The problem

A rhythm game has to decide what judgment a tap deserves, and it has to play
a feedback sound for that judgment. These two things don't actually depend on
each other: any judgment rule should work on any audio backend, and any
backend should work with any judgment rule. On top of that, the game runs on
three kinds of platforms (browser, desktop, and an old emulator mode), so the
audio backend has to be swappable at runtime, not fixed at compile time.

## Why Bridge alone isn't enough

Bridge handles the two axes properly. `JudgmentSystem` holds an `AudioBackend`,
and both sides can grow new subclasses without touching the other. What Bridge
doesn't handle is a backend that doesn't already speak the `AudioBackend`
interface. The legacy DirectSound library doesn't, and I can't edit its source.
So Bridge by itself leaves that backend stranded outside the hierarchy.

## Why Adapter alone isn't enough

Adapter would get DirectSound to fit one client, but the class-explosion
problem would stay. Without the Bridge, every judgment mode would need one
class per backend: `AccuracyWebAudio`, `AccuracyNative`, `AccuracyDirectSound`,
`MarvelousWebAudio`, `MarvelousNative`, and so on. Adding one new backend
would double the count again. Adapter fixes compatibility; it doesn't fix
coupling.

## Why the wrapped class is genuinely incompatible

`LegacyDirectSoundBackend` is not just renamed — it independently fails the interface in three separate ways:

1. The parameters are `int`s instead of the `SampleId` enum.
2. The return type is a `short` status code, not an `AudioHandle`.
3. Errors come back as negative numbers plus a `DirectSoundFault` exception,
   not as `AudioException` with a `Reason`.

Any one of these would already require adaptation. Together, they make the class genuinely incompatible.

## Complexity module — dynamic implementor selection

The `Platform` enum is read at runtime, and `AudioBackendFactory` picks the
backend from it. The client code only ever sees an `AudioBackend`. The only
place in the whole project that mentions the concrete backend classes is
inside `AudioBackendFactory.forPlatform`.

## Open/Closed Principle on both axes

Adding a new judgment type means writing a new class that extends
`JudgmentSystem` and nothing else. None of the audio classes change. Adding a
new backend means writing a class that implements `AudioBackend`. None of the
judgment classes change.

## Limitation

`DirectSoundAdapter.stopAll()` does nothing, because the legacy library has no
equivalent method. A real version of this adapter would have to keep a list of
active handles and stop them one at a time. It's a real gap in how well the
adapter covers the `AudioBackend` contract, and it's directly because the
legacy library is weaker than the two modern backends. It doesn't affect the
judgment side at all, but it's a real limitation of the design.