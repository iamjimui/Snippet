import { Injectable } from '@angular/core';
import { AlertType } from '../models/alert.model';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor() { }

  show(
    type: AlertType,
    message: string,
    duration: number = 5000,
    playSound: boolean = false
  ) {

    let current = document.getElementById("current-alert");
    if (current) {
      document.body.removeChild(current);
    }

    let alert = document.createElement("div");
    alert.id = "current-alert";
    alert.classList.add("alert", `alert-${type}`, "alert-custom", "alert-fade", "row", `text-${type}`, "align-items-start", "card-wrapper");
    alert.setAttribute("style", `--animation-delay:0s, ${duration}ms`);

    alert.innerHTML = `
        <div class="col-2 p-0 d-flex align-item-center justify-content-center">
          <ion-icon class="fs-3" name="####"></ion-icon>
        </div>
        <div class="col-10 d-flex align-item-center justify-content-start">
          <span class="fw-semibold">${message}</span>
        </div>
    `;

    if (type === AlertType.danger) {
      alert.innerHTML = alert.innerHTML.replace(/####/, "ban");
    } else if (type === AlertType.success) {
      alert.innerHTML = alert.innerHTML.replace(/####/, "checkmark-done");
    } else {
      alert.innerHTML = alert.innerHTML.replace(/####/, "warning");
    }

    if (playSound) {
      let audio = new Audio(`assets/sound/${type}.mp3`);
      audio.play();
    }

    document.body.appendChild(alert);

    alert.addEventListener("click", () => document.body.removeChild(alert));
    setTimeout(() => {
      if (document.getElementById("current-alert")) {
        document.body.removeChild(alert);
      }
    }, duration + 1000);
  }
}
