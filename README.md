# RandomUserApp
## A take home challenge

### Versions
- **Android Studio:** Android Studio Arctic Fox 2020.3.1
- **Tested on Emulator:** Pixel 3a Emulator running API 31, target Android 12 preview, arm64 v8a
- **Gradle JDK**: 11.0.10 (Android Studio Default)

### Areas of Focus
- **Architecture**: I focused mainly on architecture, data flow, and modular code. I wanted to use
modern practices combined with recent libraries. I used an MVVM approach with the business logic
handled by the `ViewModel` with the intention of needing very little (if any) logic in the
`Activity` and `Fragment` classes. I used `Dagger-Hilt` for dependency injection as it
adds some desirable features for `ViewModel` injection, by nature dependency injection helps with
modularity. I used `Retrofit` with `Moshi` for network calls.
- **Testing**: I focused on unit testing for this app, I would have liked to add instrumentation 
testing but I didn't have time. Code coverage was not my goal so much as demonstrating a few
different ways to test the various components. Additional tests could be made in the same fashion 
to increase coverage.
- **Demonstrate Different Practices**: I tried to sprinkle in a little of everything in this app
while still adhearing to acceptable practices. For example, I created some one offs that are 
functional but are mostly for demonstration; like an extension function and a binding adapter.

### Additional Comments
- I built this app strictly on mobile, I did not factor in tablet design.

- I made some assumptions about the api. I didn't see a place in the documentation that said what
fields were required or not required. I found that the `id` field sometimes had null fields and
I didn't see a need to use it so I ignored it. All other fields I assumed to be required.

- There is a lot of data assosiated with each user. I didn't feel that all of it was needed. 
I took information that I thought was relevant and I could use in an interesting way. I 
didn't feel it was necessary to take certain fields because time was limited and at a certain
point it becomes an exercise in repitition. An example of this is the `login` object. I didn't
use it because I didn't have time to do something interesting with it. Given more time I might
use the AndroidKeyStore to store some of the private information. Furthermore, I could have used
the uuid as the primary key, admittedly this isn't a normal practice but it requires less code 
to access the email and for an app this size it ends up being a concept more than a necessity.

- I do not consider myself a designer. I did my best to adhere to material design guidelines but 
I am in general I am not creative when it comes to design so I didn't spend a ton of time on this.
If given more time it would be nice to have some animations for the fragment transitions and
things like that but I decided my time was better spent on the architecture and data flow. 

- There are a number of additional features I was hoping to add but didn't have time to implement.
I would be happy to discuss some of them. 
