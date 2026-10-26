import { Component, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { AuthService } from "../../../../services/auth.service";

@Component({
  selector: "app-landing-cta-banner",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./cta-banner.component.html"
})
export class CtaBannerComponent {
  auth = inject(AuthService);
}
